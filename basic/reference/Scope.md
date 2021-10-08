
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

jobParameters 는 Step이나, Tasklet, Reader 등 Batch 컴포넌트 Bean의 생성 시점에 호출할 수 있지만  
정확히는 Scope Bean 을 생성할때만 가능하다.  
즉 @StepScope, @JobScope Bean을 생성할때만 jobParameters가 생성 된다.

```java
@Slf4j
@Component
//@StepScope
class ErrorJobTasklet implements Tasklet {

   @Value("#{jobParameters[requestDate]}")
   private String requestDate;

   public ErrorJobTasklet() {
      log.info(">>>> ErrorJobTasklet 생성");
   }

   @Override
   public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
      log.info(">>>> ErrorJobTasklet");
      log.info(">>>> requestDate = {}", requestDate);
      return RepeatStatus.FINISHED;
   }
}
```

위와같이 jobParameters를 설정하고 @StepScope설정을 주지 않는다면  
`SpelEvaluationException: EL1008E: Property or field 'jobParameters' cannot be found on object of type 'org.springframework.beans.factory.config.BeanExpressionContext`  
해당 에러를 만날 수 있다.  
즉 Bean을 메소드, 클래스 생성은 무방하나 Scope는 Step, Job을 가져야한다.

---

Spring Boot의 환경변수 또는 시스템 변수를 사용할시

1. 시스템 변수를 사용할 경우 Spring Batch 의 Job Parameter 관련 기능을 쓰지 못한다.
   * 같은 Job Parameter로 같은 Job을 두번 실행하지 않지만 해당 기능이 작동하지 않는다.
   * 메타 테이블의 BATCH_JOB_EXECUTION_PARAMS가 관리되지 않는다.
2. Command line이 아닌 다른 방법으로 Job을 실행하기 어렵다. (아직 무슨말인지 이해못함...)
   * 실행시 전역 상태(시스템 변수 혹은 환경 변수)를 동적으로 변경시킬 수 있도록 Spring Batch를 구성해야한다.
   * 동시에 여러 Job을 실행하려는 경우 또는 테스트 코드로 Job을 실행해야할때 문제가 발생할 수 있다.
3. Late Binding을 사용하지 못한다.
   * 외부 파라미터에 따라 Batch가 다르게 동작해야한다면, 시스템 변수로 이것을 풀어내기 어렵다.

하지만 Job Parameter를 사용하면 아래와 같이 쉽게 해결할 수 있다.
```java
@Slf4j
@RequiredArgsConstructor
@RestController
public class JobLauncherController {

    private final JobLauncher jobLauncher;
    private final Job job;

    @GetMapping("/launchjob")
    public String handle(@RequestParam("fileName") String fileName) throws Exception {

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                                    .addString("input.file.name", fileName)
                                    .addLong("time", System.currentTimeMillis())
                                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return "Done";
    }
}
```
> 웹서버에서 Batch를 관리하는 것은 권장하지 않는다.  
> 예제 코드로 확인 할 것.




