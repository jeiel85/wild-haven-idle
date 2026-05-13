# Progress

## 2026-05-13

### Done

- Initial project direction defined
- Repository name recommended: `wild-haven-idle`
- IP and ethics policy drafted
- MVP scope defined
- Android technical structure drafted
- Agent workflow rules integrated
- Initial document package generated
- Android Compose project scaffold added
- Application ID set to `com.jeiel85.wildhavenidle`
- MVP domain models and animal definitions added
- BalanceCalculator and unit tests added
- Simple HomeScreen MVP added
- Local Android SDK path configured through untracked `local.properties`
- DataStore save/load added for MVP game state
- Home screen now ticks care points and shows offline reward on return
- GitHub Actions passed for PR #1 after fixing Gradle Wrapper execute permission
- Release keystore created outside the repository
- Release signing secrets registered in GitHub Actions
- Signed release APK and AAB generated locally
- Signed release APK and AAB generated in GitHub Actions Android CI for PR #2
- App launcher icon generated and wired into Android resources
- Release workflow publishes signed APK/AAB to GitHub Releases on `v*` tag push
- GitHub Pages page surfaces a Download APK call-to-action linking to latest release

### 2026-05-13 (afternoon)

- Compose Navigation dependency added (navigation-compose 2.9.0)
- AppNavGraph with 4 routes (Home, Animals, Archive, Settings) created
- AnimalListScreen + ViewModel: 구조 동물 목록, 회복 지원, 보호구역 업그레이드
- ArchiveScreen + ViewModel: 도감 화면 (LOCKED/DISCOVERED/PROTECTED 상태 표시)
- SettingsScreen: 데이터 초기화 (확인 대화상자 포함), 버전 정보
- GameRepository 확장: supportAnimalRecovery, upgradeSanctuary, resetData, applyUnlocks
- BalanceCalculator.getRecoveryBaseCost 추가 (희귀도별 회복 비용)
- CheckUnlockConditionsUseCase 추가: CarePoint/회복단계/생산량/보호동물수 조건 체크
- HomeScreen에 네비게이션 버튼 추가 (구조 동물, 도감, 설정)
- MainActivity에서 AppNavGraph 사용하도록 변경
- 해금 조건 자동 체크: 상태 변경 시마다 신규 동물 해금 확인
- Build + test 통과 확인

### Not Done

- Remote repository main branch not confirmed
- Separate manual/tag release workflow has not been triggered yet (first `v*` tag not pushed)
- Art assets are placeholder/generated; self-made art not yet created
- Localization resources (Korean/English) not yet added
- Unit tests for new screens/use cases not yet added
