# rabbit_001 — DALL·E 3 vs Imagen 3 시연(bake-off)

본작 첫 일러스트(`wh_animal_rabbit_001`, 숲토끼)를 두 서비스에서 같은 프롬프트로
4장씩 생성한 뒤, `docs/ART_DIRECTION.md` §8.7의 5-게이트로 비교해 *본작 톤에
더 맞는 한 서비스를 잠근다*. 한 번 잠그면 이후 모든 일러스트(여우/사슴/부엉이/
스라소니/보호구역 헤더/카드 배경)는 같은 서비스로만 생성한다 (혼용 금지).

---

## 0. 공통 입력

- **프롬프트:** `docs/assets/prompts/wh_animal_rabbit_001.txt` (한 글자도 바꾸지 않고 그대로 붙여넣기)
- **요청 장수:** 각 서비스에서 4장
- **저장 위치:**
  - DALL·E 3 결과 → `docs/assets/source/bakeoff/rabbit_001/dalle3-{1..4}.png`
  - Imagen 3 결과 → `docs/assets/source/bakeoff/rabbit_001/imagen3-{1..4}.png`
- **저장 후:** 채팅에 "저장 완료, 검수해줘"라고 알려주시면 에이전트가 두 폴더의
  PNG를 직접 읽어 5-게이트로 평가합니다 (Claude는 멀티모달이라 이미지를 시각적으로 봅니다).

## 1. 서비스별 사용 방법

### A. DALL·E 3 (ChatGPT Plus)

1. ChatGPT 채팅창에 새 대화 시작.
2. 프롬프트 텍스트 전체를 그대로 붙여넣기.
3. 마지막에 한 줄 추가: `Use 1024x1024 square format. Generate four distinct variations, each as a separate image.`
4. 생성된 이미지를 우클릭 → 저장 → 위 경로에 `dalle3-1.png`, `dalle3-2.png`, ...

> 주의: ChatGPT Plus는 한 메시지에 보통 1장씩 생성합니다. "different variation, same style"이라고 4번 요청하거나, 한 번에 "make four variations" 요청 후 ChatGPT가 어떻게 처리하는지 확인.

### B. Imagen 3 (Gemini Advanced)

1. Gemini 앱 또는 gemini.google.com에서 새 대화.
2. 같은 프롬프트 붙여넣기.
3. 마지막에 한 줄 추가: `Make it square (1:1 aspect ratio). Generate four distinct variations.`
4. 생성된 이미지를 다운로드 → 위 경로에 `imagen3-1.png`, ...

> Imagen 3에는 SynthID 라는 보이지 않는 워터마크가 들어갑니다. 사용자 눈에 보이는 화질·구성에는 영향 없음.

---

## 2. 5-게이트 평가 (사용자 + 에이전트 공동)

각 게이트를 ✅ / ⚠️ / ❌ 로 표시하고, 4장 중 *최고 후보 1장 기준*으로 점수.

| Gate | 기준 | DALL·E 3 (best of 4) | Imagen 3 (best of 4) |
|---|---|---|---|
| 1. IP 안전 | 기존 캐릭터/브랜드/실루엣 우연 차용 없음 | ✅ 일반 사실주의 토끼, 어떤 기존 캐릭터도 연상 없음 | ✅ 안전 범위 (그림책 톤이지만 특정 IP 없음) |
| 2. 윤리 안전 | 학대·고통·공격성 없음, 안정적 보호 동물 인상 | ✅ 모두 안정·호기심 표정 | ✅ 동일 |
| 3. 톤 일치 | ART_DIRECTION §1·§3·§5와 부합 | ✅✅ "wildlife observation journal aesthetic" 키워드와 거의 정확히 부합 | ⚠️ 의도한 "조용한 관찰 노트"보다 "유아 그림책" 톤에 가까움 |
| 4. 기술 적합 | 1:1, 단일 주제 중앙, 카드 배경에 올리기 좋음 | ⚠️ 배경이 풍부해 카드 위에서 주제 가독성 보통. dalle3-4는 우하단 작은 서명 = 네거티브 위반 | ⚠️⚠️ 1장 안에 4컷 그리드 + 각 패널 액자 마진 → 카드 단일 주제 격리가 어려움 |
| 5. 일관성 잠재력 | 4장 사이 화풍·색감 분산 정도 | ✅ 4장 모두 같은 작가 시리즈처럼 보이는 수준의 일관성 | ⚠️ 4컷 사이에서도 외곽선 굵기/채도/액자 처리 변화 눈에 띔 |

### 자유 메모

- DALL·E 3:
  - 19세기 자연사 박물지 / Beatrix Potter류 *세밀 수채화* 톤. 따뜻한 아이보리 배경, 풍부한 풀밭·이끼·야생화. ART_DIRECTION 키워드(*storybook illustration / warm watercolor / wildlife observation journal*)와 정확히 부합.
  - 후보 1순위: **dalle3-1** — 배경이 가장 단순하고 우측 향 자세 → 카드 좌측 텍스트 배치 유리.
  - 후보 2순위: dalle3-4 — 가장 따뜻하지만 *작은 서명/이니셜이 우하단에 있음* (네거티브 위반, 채택 시 마스킹 필요).
- Imagen 3:
  - 1장 안에 4컷 그리드로 응답 (의도한 "각 1024×1024 4장"과 다름).
  - 외곽선이 또렷하고 컬러가 평면적, 채도 약간 높은 *그림책* 톤.
  - 각 패널 주변에 자동 수채 액자 마진이 붙어 카드 시스템에 그대로 쓰기 어려움.

---

## 3. 결정

- **잠금:** ☑ **DALL·E 3 (ChatGPT Plus, gpt-image-1)** / ☐ Imagen 3
- 결정 이유 (한 줄): 톤 일치도와 4장 사이 일관성에서 DALL·E 3가 압도적이며, Imagen 3는 4-in-1 그리드와 자동 액자 마진 때문에 본작 카드 시스템에 부적합하다.
- 결정 일자: 2026-05-14
- **채택본:** `dalle3-1.png`

이 결정은 `docs/ART_DIRECTION.md` §8.1 "한 번에 한 서비스만 사용" 원칙에 따라
이후 모든 본작 일러스트(여우/사슴/부엉이/스라소니 4종 + 보호구역 헤더 + 카드 배경)에
그대로 적용된다. Imagen 3는 본 프로젝트 일러스트 생성에서는 사용하지 않는다.

이 결정은 `docs/ART_DIRECTION.md` §8.1 "한 번에 한 서비스만 사용" 원칙에 따라
이후 모든 본작 일러스트에 그대로 적용된다. 변경 시 그 시점까지의 모든 에셋을
새 서비스로 재생성해야 한다.

---

## 4. 채택 후 절차 (참고)

1. 채택본 1장을 `docs/assets/source/wh_animal_rabbit_001.png`로 이름 변경 보존.
2. 1024 → 512 다운샘플 + pngquant 최적화 → `app/src/main/res/drawable-nodpi/wh_animal_rabbit_001.png`.
3. `docs/ASSET_LICENSES.md` §2 표에 한 줄 추가 (서비스/모델/프롬프트 SHA-256/생성일/수정 내역).
4. `presentation/components/AnimalIllustration.kt`의 `rabbit_001` 분기를 PNG 로딩으로 교체 (별도 PR로).
5. `CHANGELOG.md` Unreleased에 기록.

이후 동물 4종(`fox_001`, `deer_001`, `owl_001`, `lynx_001`)도 같은 서비스로 같은
프롬프트 템플릿(주제 슬롯만 교체)으로 진행.
