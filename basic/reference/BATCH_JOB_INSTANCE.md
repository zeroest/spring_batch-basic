
# BATCH_JOB_INSTANCE

---

Job Parameter 가 같으면 BATCH_JOB_INSTANCE 로우가 추가되지 않는다.

반대로 Job Parameter가 다르면 BATCH_JOB_INSTANCE 로우가 추가된다.

---

동일한 Job Parameter로 Batch를 실행하면 아래와 같은 에러를 만난다

`JobInstanceAlreadyCompleteException: A job instance already exists and is complete for parameters={nowDateTime=20210914}.  If you want to run this job again, change the parameters.`

즉 동일한 Job으로 동일한 Job Parameter는 여러개 존재할 수 없다.

