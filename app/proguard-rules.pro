# Wild Haven Idle release ProGuard / R8 rules
#
# 대부분의 keep 규칙은 사용 중인 AndroidX/Compose/DataStore 라이브러리가
# consumer rules로 자체 제공하므로 여기는 *프로젝트 고유 보존*만 둔다.
#
# 디오브퓨스케이션을 위한 mapping.txt는 AGP가 AAB 메타데이터에 자동 포함
# (Play Console이 별도 업로드 없이 사용).
#
# 디버깅 편의: 줄 번호와 소스 파일명 보존 → 스택 트레이스 가독성 향상
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# 우리 데이터 모델은 DataStore Preferences로 *문자열 인코딩*해 저장하므로
# 클래스 이름이 직렬화에 영향을 주지 않는다 (JSON/Parcel/Reflection 사용 안 함).
# Compose runtime/foundation, ViewModel, Navigation 모두 자체 consumer rules가
# 필요한 클래스를 keep한다. 추가 규칙 불필요.

# 향후 reflection 기반 라이브러리(예: kotlinx.serialization, Moshi, Retrofit) 추가 시
# 여기에 해당 keep 규칙을 명시한다.
