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

### 2022.11.16
- 메모 목록 업데이트 로직 구현
- LifecycleObserver 추가
- CoroutineDispatcher 의존성 주입 구현
- UseCase 리턴 타입 T -> Result<T>로 수정
- 회고
  > Fragment가 Activity에 비해 생명 주기가 복잡하다고 느꼈다. ViewPager2에서의 Fragment 생명 주기, Fragment Transaction 간 Fragment 생명 주기가 다른 것 같아 좀 헷갈리는 것 같다. 한 번 정리할 필요가 있어 보인다.
  
  > CoroutineDispatcher도 주입받아서 사용하는게 좋다고 어디서 본 적이 있는데, 왜 그게 좋은건지 이유를 알고 쓰는 것 같지 않다.
  
  > NotepadLifecycleObserver 객체를 주입받아서 BaseActivity, BaseFragment에서 사용하려고 했는데 실패했다. 이 이유에 대해 알아봐야 할 것 같다.

- 작성된 메모 조회 & 업데이트 로직 구현
- insert, update UseCase 통합
- 회고
  > memoId가 0이면 insert, 0이 아니라면 update하도록 로직을 작성했다. memoId에 따라 분기처리 되기 때문에 이를 ViewModel에서 하는 것보다 UseCase, 혹은 그 안쪽에서 처리하면 좋겠다고 생각했는데, 이 과정에서 단일 책임이란 무엇인가에 대해 생각해보게 되었다. insert, update를 각각 하나의 책임으로 본다면 InsertOrUpdateUseCase는 2개의 책임을 하는 클래스인가 하는 고민이 든다. 지금은 memoId로 분기처리하는 것을 Repository 구현체에서 수행하는데, 이 구조가 맞는지 더 고민해보면 좋을 것 같다.

### 2022.11.17
- 메모 삭제 로직 구현
- 회고
  > 메모 삭제 시 DAO에서 @Delete 어노테이션을 사용했는데, DTO 객체를 직접 넘기는 샘플 코드를 참고했다. 그런데 이 샘플 코드의 경우 Primary Key 값을 활용해 삭제를 수행한다고 했는데, 그러면 DTO 객체를 직접 전달하는 것보다 키 값 리스트를 전달하는게 낫지 않을까 하는 생각이 들었다. @Delete 어노테이션을 활용하면 어떤 식으로 동작하는지 정확히 알고 사용하면 더 좋을 것 같다는 생각이 든다.
- Configuration change 대응 로직 구현
- 회고
  > 기기 회전 등의 이유로 Configuration change가 발생하면, Activity는 onDestroy()를 실행한 후 onCreate()를 호출해 새 인스턴스를 만든다. 이 과정으로 인해 WriteFragment를 표시한 상태에서 기기를 회전할 경우 ListFragment로 보여지는 문제가 있었다. 이 과정에서 내가 분석한 문제점은, 첫째로 MainActivity의 onCreate() 내부에 FragmentManager를 사용해 replace를 호출한 것이 첫 번째 원인이라고 생각했다. 그래서 add로 변경한 후 다시 테스트했는데, 이번에는 두 Fragment가 겹쳐 보였다. 이를 통해 WriteFragment는 기기를 회전한다고 해서 소멸되는 것이 아니라는 것까지 알 수 있었고, 다음 과정으로는 MainActivity에서 FragmentManager의 transaction 이전에 backstack size를 검사하도록 구현했다. 일단은 이 방법으로 해결은 했는데, FragmentManager가 Configuration change 시에 어떤 방식으로 상태를 보존하는지 공부할 필요성을 느끼게 되었다.

### 2022.11.22
- 메모 다중 삭제 기능 구현
- 회고
  > 이 기능 하나를 위해 며칠간 고민을 많이 했다. selectedItems를 ViewModel이 관리해야 하는지, Adapter가 관리해야 하는지부터, UI를 어떻게 갱신해야 하는지까지 다양하게 고민을 한 것 같다. onBindViewHolder에서 payloads를 활용하는 방식에 대해서도 간단히 알아보기도 했고, 평소 잘 알고 있다고 생각했던 ViewHolder에 대해서도 스스로 반성하기도 했다. 특히 RecyclerView는 ViewHolder가 재사용된다는 특징이 있다는 것을 알고 있었음에도 불구하고 이를 까먹어서 ViewHolder에서 이전에 background 색상을 변경해놔서 스크롤 시 변경된 ViewHolder background 색상을 보기도 했다. 사실 지금의 코드(Adapter와 ViewHolder 둘 다 selectedItems를 보유하는 방식 - observe를 통한 동기화)가 옳은지 잘 모르겠다. 하지만 정말 고민을 많이 한 만큼, 이 부분에 대해 블로그로 포스팅하면서 주변 지인들에게 피드백을 구해봐야겠다고 생각했다.
  
  > 또, MenuProvider가 중복으로 add되는 현상에 대해서도 처리해줘야 했다. 나는 이 경우 View에 isMenuProviderAdded와 같은 변수를 두어 해결하긴 했는데, 이를 더 매끄러운 코드로 구현할 방법이 있는지 알아보면 좋을 것 같다.

  > 원래 지난 주말에 회고에 써놓은 것들을 학습하면서 공부하려고 했는데, 아쉽게 이 기능을 구현하면서 시간을 다 뺏기게 되었다. 그래서 너무 아쉽다. 이번 주말에는 꼭 놓친 부분들에 대해 정리해보도록 하겠다.

### 2022.11.23
- 메모 다중 삭제 과정 중 **다중 선택 모드**일 때 ViewHolder UI 업데이트 기능 구현
- 회고
  > `initSelectMode()`라는 메서드를 ViewHolder 내에 public method로 구현했다. 또, 체크박스의 경우 체크박스 아웃라인과 체크 아이콘 2개를 사용해 표시했는데, 선택된 메모일 경우 체크박스 아웃라인 + 체크박스를 표시하고, 선택되지 않은 메모일 경우 체크박스 아웃라인만 표시하도록 구현했다. 이 과정에서 고민했던 것은, 선택 모드가 될 때 모든 ViewHolder에 미선택 상태의 체크박스를 어떻게 보여줄 지 고민했다. 내가 선택한 방식은 MenuProvider를 add, remove할 때의 로직과 동일하게 ViewModel이 가지고 있는 선택된 메모들의 리스트를 observe하여 선택 모드 활성화 / 비활성화 시 MemoAdapter에게 notifyItemRangeChanged 메서드에 payloads를 전달하고, Adapter의 onBindViewHolder에서 payloads를 확인한 후 선택 모드 활성화 / 비활성화 상황 시 `initSelectMode()`를 호출하도록 구현했다.

  > 사실 시중에 출시된 앱들 중 많은 앱들이 리스트에서 다중 선택 기능을 제공한다. 그 앱들이 어떻게 구현했을지 궁금하다. 구현 방법에 대해 검색해서 적용한 것이 아니라 스스로 고민하면서 작성을 했는데, 내 방법보다 더 효율적인 방법을 찾고 싶다.

  > payloads를 사용한 방법을 채택한 이유는 ViewHolder의 View를 다시 그리는 것보다 visibility만 조작하는 것이 효율적이라고 판단했기 때문에 그렇게 했다.