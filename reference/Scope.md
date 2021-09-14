
# Scope

--- 

@StepScope  
@JobScope  
두가지 스코프가 존재 Job Parameter와 밀접한 연관이 있다.

Job Parameter를 사용하기 위해 항상 Scope를 설정해야한다.  
`@Value("#{jobParameters[파라미터명]}")`  
와 같이 SpEL로 선언해서 사용한다.

@JobScope는 Step 선언문에서 사용가능  
@StepScope는 Tasklet이나 ItemReader, ItemWriter, ItemProcessor에서 사용가능


Job Parameter의 타입으로 Double, Long, Date, String이 있다  
LocalDateTime이 없기에 String으로 받아 타입변환 필요 

---

```java
@Bean
public Step scopeStep2() {
        return stepBuilderFactory.get("scopeStep2")
        .tasklet(scopeStep2Tasklet(null)) // null할당 이는 Job Parameter의 할당이 어플리케이션 실행시에 하지 않기 때문
        .build();
        }
```

Spring Bean의 기본 Scope는 singleton인데  
Spring Batch 스코프의 경우 아래와 같이 동작한다.

@JobScope - Job의 실행시점에 Bean이 생성  
@StepScope - Step의 실행시점에 해당 컴포넌트를 Spring Bean으로 생성

즉 Bean의 생성 시점을 지정된 Scope가 실행되는 시점으로 지연시킨다.
또한 해당 Job, Step의 로직이 끝나면 삭제한다.

Bean 생성 지연으로 얻는 이점  
1. JobParameter의 Late Binding이 가능  
   * Job Parameter가 StepContext 또는 JobExecutionContext 레벨에서 할당가능
   * 어플리케이션 실행시점이 아닌 비즈니스 로직 처리 단계에서 Job Parameter를 할당시킬 수 있다.
2. 동일한 컴포넌트를 병렬 혹은 동시에 사용가능
   * Step안에 Tasklet이 있고, Tasklet은 멤버 변수와 이 멤버 변수를 변경하는 로직이 있는 경우
   * @StepScope 없이 Step을 병렬로 실행시키면 여러 Step에서 하나의 Tasklet을 두고 상태변경 경쟁
   * @StepScope가 있다면 각각의 Step에서 별도의 Tasklet을 생성하고 관리하게 되어 서로의 상태를 침범하지 않는다.

---






