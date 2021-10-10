
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