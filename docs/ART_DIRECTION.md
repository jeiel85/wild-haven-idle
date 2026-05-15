# ART_DIRECTION.md

# Wild Haven Idle 아트 방향

## 1. 핵심 아트 정체성

Wild Haven Idle의 시각적 정체성은 전자펫 장난감이나 기존 캐릭터 IP가 아니라, 따뜻한 야생동물 보호구역과 자연 관찰 노트에서 출발한다.

추천 키워드:

- 미니멀 라인 아트
- 자연 관찰 노트
- 종이 질감
- 보호구역 지도
- 부드러운 숲 색감
- 따뜻한 베이지/세이지 그린
- 조용한 힐링 감성

---

## 2. 피해야 할 방향

다음 시각 요소는 피한다.

- 흑백 LCD 전자 장난감 화면
- 알 모양 디바이스 UI
- 기존 전자펫 기기와 유사한 외곽 프레임
- 8비트 전자펫 감성
- 특정 기존 캐릭터를 연상시키는 실루엣, 표정, 색 조합
- 기존 게임 UI의 화면 구성 복제
- “추억의 전자펫”으로 보이는 마케팅 이미지

---

## 3. 권장 컬러 방향

| 용도 | 색감 |
|---|---|
| 기본 배경 | 따뜻한 아이보리, 연한 베이지 |
| 메인 포인트 | 세이지 그린, 포레스트 그린 |
| 보조 포인트 | 연한 하늘색, 흙색 |
| COMMON | 회색/연녹색 |
| UNCOMMON | 초록 |
| RARE | 파랑 |
| EPIC | 보라 |
| LEGENDARY | 금색 |

---

## 4. UI 원칙

- 숫자는 크게 표시한다.
- 핵심 행동 버튼은 명확하게 표시한다.
- 한 화면에서 사용자가 할 수 있는 행동을 2~3개로 제한한다.
- 보상 수령과 보호구역 개선 버튼이 즉시 보여야 한다.
- 복잡한 설명보다 짧은 문구와 시각적 피드백을 우선한다.
- 동물의 상태를 부정적 고통보다 긍정적 회복과 적응으로 표현한다.

---

## 5. 동물 표현 원칙

- 현실 동물을 기반으로 한다.
- 귀엽게 단순화하되 특정 IP 캐릭터처럼 보이지 않게 한다.
- 과도한 의인화는 피한다.
- 아프거나 고통받는 표현은 최소화한다.
- 회복 단계가 높아질수록 표정, 자세, 주변 환경이 안정되어 보이게 한다.

---

## 6. 에셋 라이선스 원칙

모든 에셋은 다음 중 하나여야 한다.

- 직접 제작
- CC0
- 상업적 사용 가능 라이선스
- 유료 구매 후 게임 사용 허용 라이선스 확인 완료

모든 에셋 출처는 `docs/ASSET_LICENSES.md`에 기록한다.

---

## 7. 앱 아이콘 방향

- 보호구역을 상징하는 숲과 잎 방패를 중심 모티프로 사용한다.
- 구조 동물은 소유 대상이 아니라 보호구역 안의 보호 동물 실루엣으로 표현한다.
- 텍스트, 알, 전자 장난감 프레임, 먹이 도구, 기존 IP를 연상시키는 형태는 사용하지 않는다.
- 작은 크기에서도 숲 보호구역과 보호/회복 정체성이 읽히도록 단순한 중심 실루엣을 유지한다.

---

## 8. AI 에셋 생성 파이프라인

본 프로젝트의 게임 내 일러스트(동물 5종, 보호구역 풍경, 카드 배경 등)는
*AI 생성 + 직접 큐레이션* 방식으로 만든다. 이유는 본작의 시각 정체성 핵심이
"구조 동물 + 보호구역" 같은 *주제가 좁고 톤 통일이 중요한 일러스트*이기 때문에,
공개 에셋 팩(Kenney 등)을 조합하기보다 일관된 톤을 직접 잡는 편이 결과가 좋다.

### 8.1 사용 가능한 서비스 (모두 상업 사용 OK 확인)

| 서비스 | 모델 | 상업 라이선스 요점 | 비고 |
|---|---|---|---|
| Adobe Firefly | Image Model 4 | 학습 데이터가 Adobe Stock + 라이선스/퍼블릭 도메인 — 출력물 상업 사용 명시적으로 안전 | Creative Cloud 구독 필요. 가장 보수적인 IP 위험 관리. |
| OpenAI gpt-image-1 / DALL·E 3 | gpt-image-1 | OpenAI 약관상 출력물 권리는 사용자에게 양도, 상업 사용 가능 | ChatGPT Plus 또는 API. 한국에서 접근성 좋음. |
| Google Imagen on Vertex AI | imagen-3 / imagen-4 | Vertex AI 이용약관 기준 상업 사용 가능 | GCP 셋업 필요. 이미 GCP를 쓰는 경우에 적합. |

**선정 원칙:** 첫 에셋을 생성하는 시점에 위 중 *하나*를 골라 고정한다. 이후 모든
일러스트는 동일 서비스/동일 모델로만 생성해 톤 일관성을 유지한다. 다른 서비스로
바꾸려면 해당 시점까지의 산출물을 모두 새 서비스로 재생성하는 것을 전제한다
(부분 혼용 금지).

**선정 결과 (잠금, 2026-05-14):** ✅ **OpenAI gpt-image-1 / DALL·E 3
(via ChatGPT Plus)**.

근거: 본작 사용자가 ChatGPT Plus와 Gemini Advanced를 모두 보유한 상황에서, 첫
에셋(`wh_animal_rabbit_001`)을 두 서비스로 동일 프롬프트 시연한 결과
(`docs/assets/bakeoff/rabbit_001-bakeoff.md` 참조), DALL·E 3가
"wildlife observation journal aesthetic" 톤과 4장 사이 일관성에서
Imagen 3를 명확히 앞섰다. Imagen 3는 4-in-1 그리드 + 자동 수채 액자 마진 때문에
카드 시스템에 부적합. 추가 비용 0 (구독 안에서 처리).

이 잠금은 본작의 모든 일러스트(동물 5종 + 보호구역 헤더 + 카드 배경 + 향후 추가
일러스트)에 적용된다. Adobe Firefly와 Imagen 3는 본 프로젝트 일러스트에 사용하지
않는다. 변경 시 그 시점까지의 모든 에셋을 DALL·E 3가 아닌 새 서비스로 재생성한다.

### 8.2 공통 프롬프트 템플릿

모든 일러스트 프롬프트는 다음 5개 슬롯을 채워서 만든다. 슬롯 순서를 지키면
서로 다른 에셋도 톤이 흩어지지 않는다.

```text
[Subject]    무엇을 그릴지 (예: A red fox curled up resting in tall meadow grass)
[Setting]    어디에 있는지 (예: in a small forest sanctuary clearing, dappled afternoon light)
[Style]      그림 스타일 앵커 (고정값, 8.3 참조)
[Palette]    색감 (고정값, 본 문서 §3 권장 컬러 사용)
[Negative]   금지 요소 (고정값, 8.4 참조)
```

조립 예 (구조 동물 카드용):

```text
A small cottontail rabbit nibbling clover at the edge of a quiet sanctuary clearing,
in a small protected forest with soft moss and a low wooden fence behind,
gentle storybook illustration, warm watercolor texture, hand-drawn outline,
warm ivory background, sage and forest green palette with soft sky-blue accents,
no text, no UI elements, no electronic device frames, no eggs, no feeding tools,
no resemblance to existing virtual pet characters or franchises.
```

### 8.3 스타일 앵커 (고정 문자열)

이 문장을 모든 프롬프트의 [Style] 슬롯에 *그대로* 넣어 톤을 통일한다.

```text
gentle storybook illustration, warm watercolor texture, hand-drawn outline,
soft natural lighting, calm wildlife observation journal aesthetic,
flat composition with subtle paper grain
```

### 8.4 네거티브 프롬프트 (고정 문자열)

```text
no text, no logos, no UI elements, no health bars, no buttons,
no electronic device frames, no LCD screen, no virtual pet device shape,
no eggs, no feeding tools, no resemblance to existing virtual pet characters
or franchises, no realistic suffering or wounds, no aggressive expressions,
no human faces, no anthropomorphic clothing
```

### 8.5 시드/일관성 관리

- **Adobe Firefly:** Reference Image 기능으로 첫 에셋(예: `rabbit_001`)을 *스타일
  레퍼런스*로 잠그고 이후 모든 동물 생성 시 이 레퍼런스를 항상 첨부한다.
- **OpenAI gpt-image-1:** Reference 기능이 없으므로 위 8.3/8.4 고정 문자열을 매번
  포함하고, 첫 에셋 생성 후 그 이미지를 사람이 보면서 "이 스타일로" 비교 검수한다.
- **Imagen on Vertex:** 시드 파라미터 지원 — 첫 에셋의 시드를 기록하고 같은 시드
  + 같은 스타일 슬롯으로 후속 생성.

선택한 서비스와 시드/레퍼런스 정보는 `docs/ASSET_LICENSES.md`의 *생성 메타데이터*
컬럼에 같이 기록한다.

### 8.6 Play Console 공시

본작은 사용자가 앱 내에서 AI를 호출하지 않으며, 일러스트는 출시 전에 *개발자가
미리 생성해 번들에 포함*한다. 이 경우 Play Console의 "Generative AI" 정책은
대부분 적용되지 않지만, 다음은 출시 전에 확인한다.

- 콘텐츠 등급 설문에서 AI 생성 시각 자산 관련 항목이 있는 경우 정직하게 답한다.
- 스토어 설명에서 "AI가 그렸다"를 영업 포인트로 강조하거나 숨기거나 하지 않는다.
- 출력물에서 기존 IP가 우연히 학습돼 섞여 있지 않은지 사람이 한 번 더 확인 (8.7).

### 8.7 검수 게이트 (사람 손)

생성된 모든 후보 이미지는 다음을 통과해야 게임 번들에 포함될 수 있다.

1. **IP 안전:** 기존 캐릭터/브랜드/실루엣이 우연히 섞이지 않았는가? 의심되면 폐기.
2. **윤리 안전:** 동물이 학대/고통 표현이 아닌가? 회복 단계에 맞는 안정감인가?
3. **톤 일치:** 기존 에셋과 같은 화풍·색감인가? 옆에 두고 확인.
4. **기술 적합:** 배경 투명/단색 처리, 필요한 해상도(아래 §9 참조), 파일 크기 적정.
5. **라이선스 기록:** §10의 절차로 `ASSET_LICENSES.md`에 즉시 기록.

기준 미달은 폐기 또는 재생성. 1차 통과율은 30~60% 정도로 잡고 시간을 본다.

---

## 9. 에셋 디렉터리 규약

| 위치 | 용도 | 형식 |
|---|---|---|
| `docs/assets/source/` | 생성 원본 (고해상도, 버전 보존용) | PNG (≥ 1024×1024) |
| `app/src/main/res/drawable-nodpi/` | 게임 내 일러스트 (해상도 독립) | PNG (≤ 512×512 권장, 압축 후 ≤ 80KB 목표) |
| `app/src/main/res/drawable/` | UI 아이콘 (벡터) | XML Vector |
| `app/src/main/res/mipmap-*/` | 앱 런처 아이콘 (해상도별) | PNG |
| `store-graphics/` | Play Store 마케팅 자산 | PNG/JPEG |
| `store-release-notes/` | 버전별 출시 노트 | TXT (`<ko-KR>`/`<en-US>` 태그) |

### 9.1 파일명 규칙

소문자 + 언더스코어 + 도메인 prefix `wh_`. 예:

```text
wh_animal_rabbit_001.png       # 동물 일러스트 (id는 AnimalDefinitions와 동일)
wh_animal_fox_001.png
wh_habitat_forest_001.png      # 서식지 배경
wh_card_bg_recovery.png        # UI 카드 배경
wh_decoration_tree_oak.png     # 보호구역 장식 요소
```

### 9.2 해상도 가이드

DALL·E 3 (잠금 서비스)의 출력 사이즈에 맞춰 다음과 같이 운용한다.

- **동물 카드 일러스트** — DALL·E 3 정사각 1024×1024 (실제 1254×1254 정도로 살짝 큼) → `docs/assets/source/`에 원본 보관 → `drawable-nodpi/`에 512×512 PNG (LANCZOS 다운샘플 + PIL 256-color quantize, 약 200 KB).
- **보호구역 헤더 풀폭 일러스트** — DALL·E 3 가로 1792×1024 (실제 1672×941 정도) → `docs/assets/source/`에 원본 보관 → `drawable-nodpi/`에 960×540 PNG (16:9 유지, 약 400 KB).
- **카드 배경** — 패턴/무지 위주, 가능하면 9-patch 또는 vector. PNG로 가는 경우 800×400.
- 모든 PNG는 `pngquant` 또는 PIL `quantize(colors=256)` 등으로 양자화 후 커밋.

> 메모: DALL·E 3는 정확히 1024 또는 1792 픽셀 정수가 아닌 살짝 다른 값으로
> 출력하는 경우가 잦다 (예: 1254, 1672). 다운샘플 시점에 정확한 *목표 해상도*로
> LANCZOS 리사이즈하므로 원본 사이즈는 그대로 보존한다.

---

## 10. 생성 후 워크플로 (요약)

1. 위 §8.2 템플릿으로 프롬프트 조립.
2. 선택한 서비스에서 4~8장 후보 생성.
3. §8.7 게이트로 사람 검수, 1장 채택.
4. 원본을 `docs/assets/source/wh_*.png`에 저장.
5. 게임 번들용으로 리사이즈/최적화하여 `app/src/main/res/drawable-nodpi/wh_*.png`로 저장.
6. `docs/ASSET_LICENSES.md`에 한 줄 추가:
   - 파일 경로, 사용 서비스/모델/버전, 프롬프트 요약 또는 해시, 시드/레퍼런스, 생성일, 수정 여부, 라이선스 분류.
7. 코드(예: `AnimalIllustration`)에서 PNG 참조로 교체하는 PR을 별도로 만든다.
8. 변경 내역을 `CHANGELOG.md`의 Unreleased에 "Added: 동물 일러스트 N종 PNG 적용" 형식으로 기록.

이 절차를 어겨도 빌드는 되지만, 출처를 추적할 수 없는 에셋이 섞이는 순간 IP/라이선스
사고로 이어질 수 있어 모든 단계가 필수다.

---

## 11. 에셋 분류 (keep / repurpose / replace)

이미 도입된 에셋도 시간이 지나면 시각 정체성 변화·작은 화면 가독성·새 카드 구성과의
부조화 등으로 재검토가 필요할 수 있다. 이때 *에셋을 무작정 삭제하거나 새로 생성하기
전*에, 다음 세 가지 중 하나로 먼저 분류해 본 문서 §11.1 또는
`.agent/session-handoff.md`에 기록한다. (분류가 끝난 다음에야 교체/재생성 단계로
넘어간다 — 게임 빌드에서 에셋을 즉시 제거하는 것은 금지.)

### 11.1 분류 정의

- **keep** — 동물/서식지 정체성에 맞고, 다른 에셋들과 스타일 일관성이 유지되며,
  현재 사용 위치(메인 화면, 도감, 보상 등)에서 가독성/위계에 문제가 없다.
- **repurpose** — 현재 사용 위치에서는 과하거나 조화롭지 못하지만, 다른 컨텍스트
  (도감, 보상 화면, 보조 카드, 마케팅 그래픽 등)에서 활용 가능하다. 즉시 폐기하지
  않고 후속 작업에서 보조 위치로 이동한다.
- **replace** — 그림체가 다른 시리즈와 어긋나거나, 전자펫/기존 IP 인상이 있거나,
  작은 크기(예: 휴대폰 360dp)에서 식별이 어려워 UI 가독성을 떨어뜨린다.
  즉시 제거가 아니라 *후속 작업으로 새 에셋을 생성한 뒤 PR에서 한 번에 교체*한다.

### 11.2 현재 분류 (2026-05-15 기준)

| 분류 | 에셋 | 비고 |
|---|---|---|
| keep | `wh_animal_rabbit_001.png` | 5종 시리즈의 기준 톤 — 변경 시 시리즈 전체 재생성 트리거 |
| keep | `wh_animal_fox_001.png` | rabbit과 동일 작가 시리즈 인상 |
| keep | `wh_animal_deer_001.png` | rabbit과 동일 작가 시리즈 인상 |
| keep | `wh_animal_owl_001.png` | 황혼 라벤더 톤, 시리즈 일관성 유지 |
| keep | `wh_animal_lynx_001.png` | 초겨울 침엽수림 톤, 시리즈 일관성 유지 |
| keep | `wh_habitat_forest_001.png` | 헤더 풀폭 풍경, 동물 산책 위에 좌상단 비움 제약 충족 |
| keep | `ic_launcher*.png` (앱 아이콘) | 보호구역 잎 방패 + 구조 동물 실루엣 모티프 |
| keep | `AnimalSilhouette` 절차적 Canvas (LOCKED 상태) | 그레이 통일감을 위해 의도적 보존 |
| review | `store-graphics/**` (Play Store 마케팅 자산) | 실기기 스크린샷과 함께 묶음으로 재검토 |
| repurpose | (현재 후보 없음) | — |
| replace | (현재 후보 없음) | — |

분류를 변경할 때는 변경 사유(예: "작은 화면에서 식별 어려움", "다른 동물과 톤 어긋남")를
짧게 기록한다. 변경한 분류는 `HISTORY.md`와 `.agent/progress.md`에 함께 남긴다.

### 11.3 의심 에셋 처리 절차

1. 분류를 `keep`에서 `repurpose` 또는 `replace`로 옮기는 결정은 *적어도 2개의
   증거*에 근거한다. 예: (a) 360dp Preview에서 실제 잘림/식별 실패가 보이고,
   (b) 다른 동물 카드와 옆에 두었을 때 톤이 어긋난다.
2. 결정이 서면, §11.2 표에 분류와 사유를 기록한다. 이 시점까지 코드/빌드에서
   에셋을 *제거하지 않는다*.
3. 새 에셋이 필요하면 §8~§10 파이프라인으로 후보를 생성·검수한다.
4. 한 PR에서 *기존 에셋 제거 + 새 에셋 도입*을 묶어 처리하고, 회귀 위험을 줄이기
   위해 한 번에 하나의 에셋만 교체한다.

이 절차의 목적은 "내 마음에 들지 않아 보인다"는 즉흥 판단으로 시리즈 일관성과
사용자 시각 기억을 무너뜨리는 것을 막는 것이다. 분류는 빠르게, 교체는 천천히.
