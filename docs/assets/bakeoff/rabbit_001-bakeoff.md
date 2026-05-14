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
| 1. IP 안전 | 기존 캐릭터/브랜드/실루엣 우연 차용 없음 | (채우기) | (채우기) |
| 2. 윤리 안전 | 학대·고통·공격성 없음, 안정적 보호 동물 인상 | (채우기) | (채우기) |
| 3. 톤 일치 | ART_DIRECTION §1·§3·§5와 부합 (따뜻한 보호구역, 잎 스토리북) | (채우기) | (채우기) |
| 4. 기술 적합 | 1:1 비율, 단일 주제 중앙, 카드 배경에 올리기 좋은 구도, 텍스트/UI 없음 | (채우기) | (채우기) |
| 5. 일관성 잠재력 | 4장 사이 화풍·색감 분산 정도 (분산 작을수록 좋음 — 다음 4종 생성 때 동일 톤 재현 유리) | (채우기) | (채우기) |

### 자유 메모

- DALL·E 3:
  -
- Imagen 3:
  -

---

## 3. 결정

- [ ] **잠금:** ☐ DALL·E 3 / ☐ Imagen 3
- 결정 이유 (한 줄):
- 결정 일자:

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
