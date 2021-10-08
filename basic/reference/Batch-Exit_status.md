
# Batch Status vs. Exit Status

> BatchStatus(Enum)  
> ExitStatus(Enum X)  
> ExitStatus는 커스터마이징 가능하다
 
BatchStatus는 Job 또는 Step 의 실행 결과를 Spring에서 기록할 때 사용하는 Enum  
COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN

`.on("FAILED").to(stepB())`  
위 코드에서 참조하는 값은 BatchStatus가 아니라 Step의 ExistStatus이다

기본적으로 BatchStatus와 ExitStatus 코드는 동일하다

---

ExitStatus를 커스터마이징 하려면 [SkipCheckingListener](../src/main/java/me/zeroest/spring_batch/exit_code/SkipCheckingListener.java)를 참조하자
