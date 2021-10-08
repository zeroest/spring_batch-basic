
# Next problem

---

Next로만 flow를 이어주면 앞의 step에서 오류가 발생시 나머지 뒤의 step은 실행되지 않는다.  
상황에 따라 분기할 수 있게 해야한다.

* .on()
  * 캐치할 ExitStatus 지정
  * \* 일 경우 모든 ExitStatus가 지정된다.
* to()
  * 다음으로 이동할 Step 지정
* from()
  * 일종의 이벤트 리스너 역할
  * 상태값을 보고 일치하는 상태라면 to()에 포함된 step을 호출합니다.
  * step1의 이벤트 캐치가 FAILED로 되있는 상태에서 추가로 이벤트 캐치하려면 from을 써야만 함
* end()
  * end는 FlowBuilder를 반환하는 end와 FlowBuilder를 종료하는 end 2개가 있음
  * on("*")뒤에 있는 end는 FlowBuilder를 반환하는 end
  * build() 앞에 있는 end는 FlowBuilder를 종료하는 end
  * FlowBuilder를 반환하는 end 사용시 계속해서 from을 이어갈 수 있음

