# GameAnvil 템플릿

GameAnvil 1.2.0 서버 제작을 위한 템플릿입니다. 아래 가이드를 따라 개발 환경 설정을 끝낸 뒤에 설정 및 구동이 가능합니다.

### 개발 환경 설정

**프로젝트 JDK 버전 설정**
1. 상단의 도구 모음줄에서 File > Project Structure를 선택합니다.
2. Project Setting > Project 단락에 Project JDK 버전을 선택합니다.

**Maven 프로필 설정**
1. 화면 오른쪽의 Maven 버튼을 눌러서 탭을 열 수 있습니다.
2. Reload All Maven Project버튼(Maven 패널의 왼쪽 위에 있는 돌아가는 화살표 모양) 을 클릭해서 Profile 단락에서 JDK 버전과 맞는 프로필이 선택 되어있는지 확인합니다.

**빌드 JDK 설정**
1. 빌드 아이콘 옆의 실행 환경 설정 정보(Run With...)를 클릭해서 드롭 다운 박스를 연 뒤, Edit Configuration을 클릭합니다.
2. Build And Run 단락에서 알맞은 자바 버전을 선택합니다.

### 개발 환경 설정 트러블슈팅
메이븐의 Profile이 프로젝트의 JDK 설정에 맞추어 자동으로 설정 되지 않는 경우에는 아래와 같이 설정합니다.

**Maven Importer JDK 설정**
1. 상단의 도구 모음줄에서 IntelliJ > Preference 를 선택합니다.
2. Maven > Importing 단락에서 JDK for importer 항목을 Use Prjoect JDK 로 설정합니다.

### JIT, AOT 방식의 컴파일과 Quasar 라이브러리의 특징

프로그래밍 코드는 컴파일러에 의해 기계어로 번역됩니다.
실행 전 미리 컴파일을 수행해놓는 방식을 AOT(Ahead of Time)방식이라고 하고,
반대로 컴파일러를 실행 파일에 포함하여 실행 직전 컴파일을 수행하는 방식을 JIT(Just in Time)방식이라고 합니다.

Quasar 라이브러리는 자체적인 Fiber 기반의 코드를 지원하기 위해 특수한 예외 (SuspendExecution)와 어노테이션(@Suspendable)을 사용하는데,
런타임에 이것들이 의도한 방식으로 바꿔 실행되게 하기 위해서 AOT 방식으로 바이트 코드를 조작하여 바이트코드 레벨에서 Instrumentation를 수행합니다.

GameAnvil은 이 템플릿에서 두 가지 컴파일 방식을 모두 사용합니다.
IDE에서 실행할 때는 JIT 방식으로 컴파일을 수행하고, maven을 통해 빌드하면 AOT 방식을 사용하도록 미리 설정 되어있습니다.

### GameAnvilConfig.json 설정하기

src > main > resources 의 GameAnvilConfig.json 을 이용하여 서버를 설정할 수 있습니다.

**common**
* **ip** 서버의 ip 주소를 설정합니다.
* **meetEndPoints** ipc 노드가 통신을 위해 사용할 포트를 등록합니다.
* **debugMode** 개발 중에 사용하는 옵션으로, true로 설정할 경우 각종 timeout이 발생하지 않습니다.

**location**
* **clusterSize** 서버가 총 몇 개의 머신으로 구성되는지 설정합니다.
* **replicaSize** 복제본을 포함한 그룹의 크기를 설정합니다.
* **shardFactor** 데이터 분산 위한 인수입니다.

**match**
* **nodeCnt** 매치메이킹을 담당하는 노드의 개수를 설정합니다.

**gateway**
* **nodeCnt** 연결을 관리하는 노드의 개수를 설정합니다.
* **ip** 클라이언트와 연결될 IP를 설정합니다.
* **dns** 클라이언트와 연결될 도메인 주소를 설정합니다.
* **connectionGroup** 보안 연결을 위한 설정입니다.

**game**
* **nodeCnt** 게임과 관련된 작업을 담당하는 노드의 개수를 설정합니다.
* **serviceId** 해당 게임 노드의 서비스 아이디를 설정합니다.
* **serviceName** 해당 게임 노드의 서비스 이름을 설정합니다.
* **chanelIDs** 해당 게임 노드의 채널 갯수와 아이디를 설정합니다.
* **userTimeout** 클라이언트가 접속이 끊겼을 때, 해당 유저를 서버에서 유지하는 시간을 설정합니다.

이 외에도 서버의 설정을 더 세세하게 구분해서 제공하고 있습니다. 서버 설정에 대해 더 자세하게 알고 싶다면, [서버 구성과 구동 가이드](https://docs.toast.com/ko/Game/GameAnvil/ko/server-16-config-vm/) 를 참고하세요.
### 코드 작성 방법
**프로토콜 작성**

클라이언트와 서버 간 통신을 위한 프로토콜로 Protocol Buffer를 사용합니다. 아래는 C# 클라이언트를 위한 프로토콜 작성 튜토리얼입니다.
1. User.proto 파일을 생성합니다.
2. 아래와 같이 작성합니다.
```protobuf
message Person {
  required string name = 1;
  required int32 id = 2;
  optional string email = 3;
}
```
3. 터미널에서 아래 명령어를 이용해서 컴파일합니다.
```
./protoc  ./src/main/proto/User.proto --java_out=./src/main/java --csharp_out=./
```
4. 컴파일된 결과가 각 설정한 경로에 저장됩니다.

프로토콜 버퍼에 대해서 더 자세히 학습하기 위해서는 [프로토콜 버퍼 가이드](https://developers.google.com/protocol-buffers/docs/overview) 를 참고합니다.

**서버 코드를 빠르게 작성하는 팁**

게임엔빌에서 제공하는 파일 템플릿을 이용하면, 각 노드와 룸, 유저를 빠르게 작성할 수 있습니다. 아래의 파일 템플릿을 기본으로 제공 중이며, 파일 템플릿 사용은 src 디렉터리를 포함한 그 하위 디렉터리에서 우클릭으로 컨텍스트 메뉴를 연 뒤 New를 선택하여 사용할 수 있습니다.

* Connection
* Session
* GatewayNode
* GameNode
* GameRoom
* GameUser
* RoomMatchForm
* RoomMatchInfo
* RoomMatchMaker
* UserMatchInfo
* UserMatchMaker
* SupportNode

### 더 참고할만한 문서

* [게임엔빌 개요](https://docs.toast.com/ko/Game/GameAnvil/ko/overview/)
* [게임엔빌 직소 퍼즐 게임 제작 튜토리얼](https://docs.toast.com/ko/Game/GameAnvil/ko/tutorial/)
* [게임엔빌 서버 개발 가이드](https://docs.toast.com/ko/Game/GameAnvil/ko/server-01-getting-started/)
