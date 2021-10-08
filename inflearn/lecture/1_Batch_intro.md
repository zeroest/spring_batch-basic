
# 개요 및 아키텍처

## 핵심 패턴

- Read(Extract) - 데이터베이스, 파일, 큐에서 다량의 데이터 조회.
- Process(Transform) - 특정 방법으로 데이터를 가공.
- Write(Load) - 데이터를 수정된 양식으로 다시 저장.

## 배치 시나리오

- 배치 프로세스를 주기적으로 커밋
- 동시 다발적인 Job의 배치 처리, 대용량 병렬 처리
- 실패 후 수동 또는 스케줄링에 의한 재시작
- 의존관계가 있는 step 여러 개를 순차적으로 처리
- 조건적 Flow 구성을 통한 체계적이고 유연한 배치 모델 구성
- 반복, 재시도, Skip 처리

## 아키텍처

### Application

- 스프링 배치 프레임워크를 통해 개발자가 만든 모든 배치 Job과 커스텀 코드를 포함
- 개발자는 업무로직의 구현에만 집중하고 공통적인 기반기술은 프레임웍이 담당하게 한다

### Batch Core

- Job을 실행, 모니터링, 관리하는 API로 구성되어 있다
- JobLauncher, Job, Step, Flow 등이 속한다

### Batch Infrastructure

- Application, Core 모두 공통 Infrastructure 위에서 빌드한다
- Job 실행의 흐름과 처리를 위한 틀을 제공함
- Reader, Processor, Writer, Skip, Retry 등이 속한다


> [Reference](https://docs.spring.io/spring-batch/docs/4.3.x/reference/html/spring-batch-intro.html#spring-batch-intro)
