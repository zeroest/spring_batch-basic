
# Decide

---

ExitStatus를 조작해서 분기처리 해야하면 어려움이 많다.  
ExitStatus를 커스텀하게 고치려면 Listener를 생성하고 Job flow에 등록해야한다.

JobExecutionDecider를 flow중간에 삽입하여  
ExitStatus로 분기처리하지 않고 쉽게 로직을 나누었다.

[reference](../src/main/java/me/zeroest/spring_batch/job/DeciderJobConfiguration.java)
