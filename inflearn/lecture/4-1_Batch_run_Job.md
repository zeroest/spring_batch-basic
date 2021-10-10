
# 배치 실행

---

# 배치 초기화 설정

## JobLauncherApplicationRunner

- Spring Batch 작업을 시작하는 ApplicationRunner 로서 BatchAutoConfiguration 에서 생성됨 
- 스프링 부트에서 제공하는 ApplicationRunner 의 구현체로 어플리케이션이 정상적으로 구동되자 마자 실행됨 
- 기본적으로 빈으로 등록된 모든 Job을 실행시킨다 

## BatchProperties

- Spring Batch 의 환경 설정 클래스 
- Job 이름, 스키마 초기화 설정, 테이블 Prefix 등의 값을 설정할 수 있다 
- application.properties 또는 application.yml 파일에 설정함 

  - spring.batch.job.names: ${job.name:NONE}
  - spring.batch.jdbc.initialize-schema: embedded, never, always (default: embedded)
  - spring.batch.jdbc.table-prefix: BATCH_ (default) 

## Job 실행 옵션

- 지정한 Batch Job만 실행하도록 할 수 있음
- spring.batch.job.names: ${job.name:NONE}
- 어플리케이션 실행시 Program arguments 로 Job 이름을 입력한다
 
  - --job.name=helloJob
  - --job.name=helloJob,simpleJob (하나 이상의 Job을 실행 할 경우 쉼표로 구분해서 입력함)


참고
BatchAutoConfiguration -> JobLauncherApplicationRunner -> setJobs -> run(ApplicationArguments args(JobParameter로 전달한 값)) -> launchJobFromProperties(Properties) -> executeLocalJobs(JobParameters)

--- 
---

# Job and Step

---

# JobBuilderFactory / JobBuilder

스프링 배치는 Job 과 Step을 쉽게 생성 및 설정할 수 있도록 util 성격의 빌더 클래스들을 제공

## JobBuilderFactory

- JobBuiler 를 생성하는 팩토리 클래스로서 get(String name) 메서드 제공
- JobBuilderFactory.get("jobName")
  - "jobName" 은 스프링 배치가 Job을 실행시킬 때 참조하는 Job 이름

## JobBuilder

- Job을 구성하는 설정 조건에 따라 두 개의 하위 빌더 클래스를 생성하고 실제 Job 생성을 위임한다

### SimpleJobBuilder

- SimpleJob 을 생성하는 Builder 클래스  
- Job 실행과 관련된 여러 설정 API를 제공한다 

### FlowJobBuilder

- FlowJob 을 생성하는 Builder 클래스 
- 내부적으로 FlowBuilder 를 반환함으로써 Flow 실행과 관련된 여러 설정 API를 제공한다 

![JobBuilder_architecture](./JobBuilder_architecture.png)

![JobBuilder_class_struct](./JobBuilder_class_struct.png )
