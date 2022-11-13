# 개발일지

### 2022.11.10
- 프로젝트 시작

### 2022.11.11
- 의존성 추가
  - Android KTX, Dagger-Hilt, Room, Timber 추가
- BaseActivity, BaseFragment 구현
- ListFragment 구현
- 회고
    > 멀티모듈을 처음으로 적용해보고 싶었지만 적절한 자료를 찾기 어려웠음 (추후 주변의 조언 및 추가적인 탐색을 통해 적절한 자료를 찾아 멀티모듈로 변경해볼 예정)
  
    > BaseActivity, BaseFragment는 매번 사용함에도 불구하고 이전 프로젝트에서 코드를 가져오는 것 같음 (좀 더 적응해야 할 필요성 느낌)
    
    > DataBinding을 사용하지 않는 앱에서는 어떤 방식으로 BaseActivity, BaseFragment를 선언하는지 모르는 것 같음

    > data class가 제공하는 기능을 정확히 알고 사용하지 않는 것 같음

### 2022.11.12
- WriteFragment 구현
- 회고
    > 평소 AppBar를 사용하지 않아 이번에 AppBar를 활용해 구현해보기로 했다. addMenuProvider를 구현한 후, 화면 전환 중 Menu가 계속해서 추가로 생성되는 문제를 발견했다. Fragment의 onDestroyView에서 removeMenuProvider를 통해 MenuProvider를 제거해주면 된다고 생각했고, addMenuProvider에서 object를 활용해 인터페이스 객체를 생성해 넣어주던 방식을 고쳐 Fragment의 멤버 변수로 선언해 주었다.
    
    > AppBar를 사용하기 위해 Android 공식 문서를 살펴봤는데, onCreateOptionsMenu 메서드는 deprecated 되었다. 공식문서는 정답이라고 생각하고 학습해 왔는데, 만약 실제로 개발하면서 공식문서를 참고한 게 아니라 이론만 공식문서로 공부했다면 아마 나는 올바른 정보를 얻지 못 했을 것이다. 가끔은 정답을 의심하는 자세를 갖는 것도 중요하다는 생각이 들었다.
 
### 2022.11.13
- Room Database 구현
- DataSource, Repository, Usecase, ViewModel 구현
- 회고
  > 제대로 공부해야겠다고 시간을 낸 게 우아한테크캠프 이후로 2개월만인 것 같은데, 그 사이에 기본적인 Room Database 사용법과 Hilt를 사용하기 위한 준비단계를 까먹은 것 같다. 반복적으로 끊임없이 학습해야겠다는 반성을 했다.
  
  > @InstallIn으로 SingletonComponent로 만들어주는 것과, @Singleton 어노테이션을 붙이는 것의 차이를 명확하게 알고 있지 않은 것 같다. 추가적인 학습이 필요한 부분이다.
  
  > data class의 val 변수에 기본 값을 부여하는 부분에 대해 학습이 필요하다고 느꼈다. 메모의 id를 auto increment 속성을 활용해보려고 했는데 헤맸다. val 변수라서 0이라는 기본값을 부여했는데, Room Database에는 auto increment에 의해 0이 아닌 다른 값이 들어간다. 이 부분에 대해 추가적인 학습이 필요하다고 생각한다.
  
  > 지금은 Flow를 활용해 Room Database의 테이블이 갱신되면 자동으로 이를 반영하게 할 수 있지만, Flow가 없던 시절에는 어떤 방식으로 데이터의 갱신을 인식할 수 있었을지 고민해보고 구현한 뒤 Flow를 적용하면 좋겠다고 생각해 남겨두었다.