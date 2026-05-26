# Play Store 그래픽 에셋

Wild Haven Idle Google Play Console 스토어 등록정보 업로드용 그래픽 에셋입니다.

## 에셋 목록

현재 Play Console 업로드용으로 바로 사용할 묶음은
[`play-console-current/`](play-console-current/)에 정리되어 있습니다.

| 파일 | 용도 | 사양 | Play Console 위치 |
|---|---|---|---|
| `icon-512.png` | 고해상도 앱 아이콘 | 512×512 PNG, 알파 가능, 1MB 이하 | 스토어 등록정보 → 그래픽 → 앱 아이콘 |
| `feature-graphic-1024x500.png` | 기능 그래픽 (스토어 배너) | 1024×500 PNG, 알파 없음, 15MB 이하 | 스토어 등록정보 → 그래픽 → 기능 그래픽 |
| `screenshots/01-home.png` | 홈 화면 스크린샷 | 1080×2240 PNG | 스토어 등록정보 → 그래픽 → 휴대전화 스크린샷 |
| `screenshots/02-animals.png` | 홈 화면의 보호 동물 영역 | 1080×2240 PNG | 동일 |
| `screenshots/03-archive.png` | 도감 | 1080×2240 PNG | 동일 |
| `screenshots/04-settings.png` | 설정 | 1080×2240 PNG | 동일 |

### 현재 업로드 묶음

| 파일 | 용도 | 사양 |
|---|---|---|
| `play-console-current/icon-512.png` | 앱 아이콘 | 512×512 PNG, 16.8 KB |
| `play-console-current/feature-graphic-1024x500.png` | 기능 그래픽 | 1024×500 PNG, 80.9 KB |
| `play-console-current/phone-screenshots/01-sanctuary.png` | 휴대전화 스크린샷 | 1080×2340 PNG |
| `play-console-current/phone-screenshots/02-animals.png` | 휴대전화 스크린샷 | 1080×2340 PNG |
| `play-console-current/phone-screenshots/03-animals-real-data.png` | 휴대전화 스크린샷 | 1080×2340 PNG |
| `play-console-current/phone-screenshots/04-restore.png` | 휴대전화 스크린샷 | 1080×2340 PNG |
| `play-console-current/phone-screenshots/05-explore.png` | 휴대전화 스크린샷 | 1080×2340 PNG |
| `play-console-current/phone-screenshots/06-settings.png` | 휴대전화 스크린샷 | 1080×2340 PNG |

## 생성 스크립트

세 가지 그래픽 작업이 Python + Pillow 기반 스크립트로 자동화되어 있습니다.

```bash
python store-graphics/generate_icon_512.py          # 1254×1254 소스 → 512×512 PNG
python store-graphics/generate_feature_graphic.py   # 1024×500 기능 그래픽 생성
python store-graphics/postprocess_screenshots.py    # 캡처된 스크린샷에서 상단 상태바 100px 잘라내기
```

브랜딩 색상은 [docs/index.html](../docs/index.html) 의 CSS 변수와 동일합니다 (`--forest`, `--leaf`, `--moss`, `--sun`, `--paper`).

---

## 스크린샷 재촬영 가이드

Play Store 요건: **휴대전화 스크린샷 최소 2장, 최대 8장**. 실제 앱 화면을 그대로 캡처해야 하며 모형/합성 이미지는 정책 위반입니다.

현재 보관된 스크린샷은 Galaxy S24 (1080×2340) 실기기에서 ADB로 자동 캡처한 뒤 `postprocess_screenshots.py` 로 상단 상태바(라이브 액티비티 포함) 100px을 잘라내 1080×2240 으로 정리한 결과입니다. 출시 후 화면이 바뀌면 동일한 방식으로 재촬영하면 됩니다.

### 자동 캡처 흐름 (참고)

```bash
# 1) 폰 USB 디버깅 활성화 + 연결
adb devices

# 2) 릴리즈 APK 빌드 및 설치
./gradlew assembleRelease
adb install -r app/build/outputs/apk/release/app-release.apk

# 3) 앱 실행 후 화면별 캡처 (네비게이션 버튼 좌표는 1080폭 기준)
adb shell am start -n com.jeiel85.wildhavenidle/.MainActivity
adb exec-out screencap -p > store-graphics/screenshots/01-home.png
adb shell input swipe 540 1850 540 760 500 && sleep 2
adb exec-out screencap -p > store-graphics/screenshots/02-animals.png
# ... 도감/설정도 동일 패턴

# 4) 상단 상태바 정리
python store-graphics/postprocess_screenshots.py
```

### 권장 화면 4종 (출시 첫 버전 기준)

| # | 화면 | 보여줄 포인트 |
|---|---|---|
| 1 | 홈 (보호 포인트 자동 적립) | 핵심 진행 화면. 보호 포인트 수치가 잘 보이는 상태 |
| 2 | 홈의 보호 동물 영역 | 회복 단계, 회복 지원 버튼 |
| 3 | 도감 | 발견/보호 상태별 분류, 잠금 동물 포함된 화면 |
| 4 | 설정 | 버전 정보, 데이터 초기화 항목 |

### 권장 사양

- 해상도: **1080 × 1920 (세로 9:16)** 또는 **1080 × 2400**
- 형식: PNG (JPG 가능)
- 가로형이 아닌 세로형으로 통일 권장
- 상태바의 시계는 자동 표시되는 그대로 OK (Play 정책상 모형 시계 합성 금지)

### Android Studio 에뮬레이터로 촬영하기

1. Android Studio → **Device Manager** → **Create Virtual Device**
2. **Phone** 카테고리에서 `Pixel 6` 또는 `Pixel 5` 선택 (1080×2400 해상도)
3. 시스템 이미지: API 34 이상 (`UpsideDownCake` 또는 `VanillaIceCream`)
4. 에뮬레이터 시작 후 앱 설치:
   ```bash
   ./gradlew installRelease
   ```
   또는 디버그 빌드:
   ```bash
   ./gradlew installDebug
   ```
5. 앱 실행 후 캡처할 화면으로 이동
6. 에뮬레이터 우측 툴바의 **카메라 아이콘** 클릭 → `screenshot_*.png`가 호스트 `Pictures/AndroidStudio` 또는 클립보드에 저장
7. 또는 `adb`로 직접 추출:
   ```bash
   adb exec-out screencap -p > screenshot_home.png
   ```

### 실기기로 촬영하기

- 안드로이드: 전원 + 볼륨 다운 동시 누름
- 캡처 직전 상태바를 정리하려면 개발자 옵션 > "데모 모드" 활용 (선택사항)
- 알림 패널, 토스트, 키보드는 보이지 않게

### 저장 위치 제안

```
store-graphics/screenshots/
  01-home.png
  02-animals.png
  03-codex.png
  04-settings.png
```

폴더가 생기면 `.gitignore` 추가 검토 불필요 — 스크린샷은 공개 자료라 저장소에 포함해도 무방합니다.

---

## 짧은 설명 / 자세한 설명 입력 위치

그래픽 외에 동일한 "스토어 등록정보" 페이지에서 입력하는 텍스트:

- **앱 이름** (30자): `Wild Haven Idle`
- **짧은 설명** (80자): `작은 야생동물 보호구역을 운영하며 구조 동물을 회복시키는 방치형 시뮬레이션.`
- **자세한 설명** (4000자): 별도 작성 필요

자세한 설명은 [docs/GAME_DESIGN.md](../docs/GAME_DESIGN.md) 의 게임 정체성·핵심 가치·핵심 게임 루프 섹션을 기반으로 작성하면 됩니다.
