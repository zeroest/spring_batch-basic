
# 배치 실행

---

# StepBuilderFactory / StepBuilder

## StepBuilderFactory

- StepBuilder 를 생성하는 팩토리 클래스로서 get(String name) 메서드 제공
- StepBuilderFactory.get("stepName");

## StepBuilder

- Step을 구성하는 설정 조건에 따라 다섯개의 하위 빌더 클래스를 생성하고 실제 Step 생성을 위임한다

### TaskletStepBuilder

- TaskletStep을 생성하는 기본 빌더 클래

### SimpleStepBuilder

- TaskletStep을 생성하며 내부적으로 청크기반의 작업을 처리하는 ChunkOrientedTasklet 클래스를 생성한다

### PartitionStepBuilder

- PartitionStep을 생성하며 멀티 스레드 방식으로 Job을 실행한다 

### JobStepBuilder

- JobStep을 생성하여 Step 안에서 Job을 실행한다 

### FlowStepBuilder

- FlowStep을 생성하여 Step 안에서 Flow를 실행한다

![StepBuilderFactory_API](img/StepBuilderFactory_API.png)

![StepBuilder_struct](img/StepBuilder_struct.png)