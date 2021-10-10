
# 배치 시작

## 배치 활성화

`@EnableBatchProcessing`

>스프링 배치가 작동하기 위해 선언해야하는 어노테이션

- 총 4개의 설정 클래스를 실행
- 스프링 배치의 모든 초기화 및 실행 구성이 이루어진다
- 스프링 부트 배치의 자동 설정 클래스가 실행됨으로  
빈으로 등록된 모든 Job을 검색해서 초기화와 동시에 Job을 수행하도록 구성됨


## 배치 초기화 설정 클래스

1. BatchAutoConfiguration

- 스프링 배치가 초기화 될 때 자동으로 실행되는 설정 클래스
- Job을 수행하는 JobLauncherApplicationRunner 빈을 생성

2. SimpleBatchConfiguration

- JobBuilderFactory 와 StepBuilderFactory 생성
- 스프링 배치의 주요 구성 요소 생성 - 프록시 객체로 생성됨

3. BatchConfigurerConfiguration

   1. BasicBatchConfigurer
   
      - SimpleBatchConfiguration 에서 생성한 프록시 객체의 실제 대상 객체를 생성하는 설정 클래스
      - 빈으로 의존성을 주입 받아서 주요 객체들을 참조해서 사용할 수 있다
   
   2. JpaBatchConfigurer
    
      - Jpa 관련 객체를 생성하는 설정 클래스 


## 배치 초기화 순서 

1. @EnableBatchProcessing  
->  
2. SimpleBatchConfiguration  
->  
3. BatchConfigurerConfiguration  
`BasicBatchConfigurer`   
`JpaBatchConfigurer`  
->  
4. BatchAutoConfiguration


## 기본 구성 

```java
@Configuration
@RequiredArgsConstructor
public class HelloJobConfiguration {

   private final JobBuilderFactory jobBuilderFactory;
   private final StepBuilderFactory stepBuilderFactory;

   @Bean
   public Job helloJob() {
      return jobBuilderFactory.get("helloJob")
              .start(helloStep())
              .build();
   }

   @Bean
   public Step helloStep() {
      return stepBuilderFactory.get("helloStep")
              .tasklet((contribution, chunkContext) -> {
                 System.out.println("Hello Spring Batch");
                 return RepeatStatus.FINISHED;
              })
              .build();
   }

}
```

1. @Configuration 선언

   - 하나의 배치 Job을 정의하고 빈 설정 

2. JobBuilderFactory

   - Job을 생성하는 빌더 팩토리 

3. StepBuilderFactory

   - Step을 생성하는 빌더 팩토리

4. Job (일, 일감)

   - helloJob 이름의 Job 생성

5. Step (단계)

   - helloStep 이름의 Step 생성

6. Tasklet (작업내용, 비즈니스 로직 구현)

   - Step 안에서 단일 태스크로 수행되는 로직 구현 

> Job 구동 -> Step 실행 -> Tasklet 실행


## 메타 데이터 스키마 

![meta-data-erd](img/meta-data-erd.png)

- 배치의 실행 및 관리를 위한 목적으로 여러 도메인들(Job, Step, JobParameters, ...)의 정보들을 저장, 업데이트, 조회할 수 있는 스키마 제공
- 실행 정보, 성공과 실패 여부 등을 관리함으로 배치운용에 있어 리스크 발생시 빠른 대처 가능
- DB와 연동시 필수적으로 메타 테이블이 생성 되어야 함

### DB 스키마 제공

- /org/springframework/batch/core/schema-*.sql
- DB 유형별 제공
- [Example mysql schema](./schema-mysql.sql)

### 스키마 생성 설정

1. 수동 생성 - 쿼리 복사 후 직접 실행

2. 자동 생성 - spring.batch.jdbc.initialize-schema 설정

   - ALWAYS
    
      - 스크립트 항상 실행
      - RDBMS 설정이 되어 있을 경우 내장 DB 보다 우선적 실행 
   
   - EMBEDDED
    
      - 내장 DB일 때만 실행되며 스키마가 자동 생성됨, 기본값

   - NEVER

      - 스크립트 항상 실행 안함
      - 내장 DB일 경우 스크립트가 생성이 안되기 때문에 오류 발생
      - 운영에서 수동으로 스크립트 생성 후 설정하는 것을 권장
      
### Job 관련 테이블

- BATCH_JOB_INSTANCE

   - Job이 실행될 때 JobInstance 정보가 저장되며 job_name과 job_key를 키로 하여 하나의 데이터가 저장
   - 동일한 job_name과 job_key로 중복 저장될 수 없다

| JOB_INSTANCE_ID | 고유하게 식별할 수 있는 기본 키 |
| --------------- | ------------------------------- |
| VERSION | 업데이트 될 때마다 1씩 증가 |
| JOB_NAME | Job을 구성할 때 부여하는 Job 이름 |
| JOB_KEY | Job_name과 jobParameter를 합쳐 해싱한 값을 저장 |

- BATCH_JOB_EXECUTION

   - job의 실행정보가 저장되며 Job 생성, 시작, 종료 시간, 실행상태, 메세지 등을 관리

| JOB_EXECUTION_ID | 고유하게 식별할 수 있는 기본 키, JOB_INSTANCE와 일대 다 관계 |
| ---------------- | ------------------------------- |
| VERSION | 업데이트 될 때마다 1씩 증가 |
| JOB_INSTANCE_ID | JOB_INSTANCE 키 저장 |
| CREATE_TIME | Execution이 생성된 시점 TimeStamp 기록 |
| START_TIME | Execution이 시작된 시점 TimeStamp 기록 |
| END_TIME | Execution이 종료된 시점 TimeStamp 기록, Job 실행중 오류가 발생해서 Job이 중단된 경우 값이 저장되지 않을 수 있음 |
| STATUS | BatchStatus 저장 (COMPLETED, FAILED, STOPPED, ...) |
| EXIT_CODE | ExitStatus 저장 (COMPLETED, FAILED, ...) |
| EXIT_MESSAGE | Status가 실패일 경우 실패 원인 등의 내용 저장 |
| LAST_UPDATED | 마지막 Execution 시점을 TimeSTamp로 기록 |

- BATCH_JOB_EXECUTION_PARAMS

   - Job과 함께 실행되는 JobParameter 정보를 저장 

| JOB_EXECUTION_ID | JobExecution 식별 키, JOB_EXECUTION 과는 일대다 관계 |
| --------------- | ------------------------------- |
| TYPE_CD | STRING, LONG, DATE, DOUBLE 타입정보 |
| KEY_NAME | 파라미터 키 값 |
| STRING_VAL | 파라미터 문자 값 |
| DATE_VAL | 파라미터 날짜 값 |
| LONG_VAL | 파라미터 LONG 값 |
| DOUBLE_VAL | 파라미터 DOUBLE 값 |
| IDENTIFYING | 식별여부 (TRUE, FALSE) |

- BATCH_JOB_EXECUTION_CONTEXT

   - Job의 실행동안 여러가지 상태정보, 공유 데이터를 직렬화(JSON)하여 저장
   - Step 간 서로 공유 가능함

| JOB_EXECUTION_ID | JobExecution 식별 키, JOB_EXECUTION 마다 각 생성 |
| --------------- | ------------------------------- |
| SHORT_CONTEXT | JOB의 실행 상태정보, 공유데이터 등의 정보를 문자열로 저장 |
| SERIALIZED_CONTEXT | 직렬화(serialized)된 전체 컨텍스트 |

### Step 관련 테이블 

- BATCH_STEP_EXECUTION

   - Step의 실행정보가 저장되며 생성, 시작, 종료 시간, 실행상태, 메세지 등을 관리 

| STEP_EXECUTION_ID | Step의 실행정보를 고유하게 식별할 수 있는 기본 키 |
| --------------- | ------------------------------- |
| VERSION | 업데이트 될 때마다 1씩 증가 |
| STEP_NAME | Step을 구성할 때 부여하는 Step 이름 |
| JOB_EXECUTION_ID | JobExecution 기본키, JOB_EXECUTION과 일대 다 관계 |
| START_TIME | Execution이 시작된 시점 TimeStamp 기록 |
| END_TIME | Execution이 종료된 시점 TimeStamp 기록, Job 실행중 오류 발생하여 Job이 중단된 경우 값이 저장되지 않을 수 있음 |
| STATUS | BatchStatus 저장 (COMPLETED, FAILED, STOPPED, ...) |
| COMMIT_COUNT | 트랜잭션 당 커밋되는 수를 기록 |
| READ_COUNT | 실행시점에 Read한 Item 수를 기록 |
| FILTER_COUNT | 실행도중 필터링된 Item 수를 기록 |
| WRITE_COUNT | 실행도중 저장되고 커밋된 Item 수를 기록 |
| READ_SKIP_COUNT | 실행도중 Read가 Skip 된 Item 수를 기록 |
| WRITE_SKIP_COUNT | 실행도중 Write가 Skip 된 Item 수를 기록 |
| PROCESS_SKIP_COUNT | 실행도중 Process가 Skip 된 Item 수를 기록 |
| ROLLBACK_COUNT | 실행도중 Rollback이 일어난 수를 기록 |
| EXIT_CODE | ExitStatus 저장 (COMPLETED, FAILED, ...) |
| EXIT_MESSAGE | Status가 실패일 경우 실패 원인 등의 내용 저장 |
| LAST_UPDATED | 마지막 Execution 시점을 TimeSTamp로 기록 |

- BATCH_STEP_EXECUTION_CONTEXT

   - Step의 실행동안 여러가지 상태정보, 공유 데이터를 직렬화(JSON)하여 저장
   - Step 별로 저장되며 Step 간 서로 공유할 수 없음 

| STEP_EXECUTION_ID | StepExecution 식별 키, STEP_EXECUTION 마다 각 생성 |
| --------------- | ------------------------------- |
| SHORT_CONTEXT | STEP의 실행 상태정보, 공유데이터 등의 정보를 문자열로 저장 |
| SERIALIZED_CONTEXT | 직렬화(serialized)된 전체 컨텍스트 |

