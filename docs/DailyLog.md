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