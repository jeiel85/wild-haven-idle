# ASSET_LICENSES.md

# Wild Haven Idle 에셋 라이선스 기록

모든 이미지, 아이콘, 폰트, 효과음, 배경음악, 일러스트, 스크린샷, 마케팅 리소스는
이 문서에 출처와 사용 권한을 기록한다. 에셋 생성/도입 절차는
`docs/ART_DIRECTION.md` §8~§10을 따른다.

---

## 1. 직접 제작 / 일반 라이선스 에셋

| Asset | File | Source | License | Commercial Use | Modified | Notes |
|---|---|---|---|---|---|---|
| App icon source | `docs/assets/wild-haven-icon-source.png` | OpenAI image generation via Codex, prompt recorded in HISTORY.md | Project-owned generated asset | Yes | Yes | Forest sanctuary shield icon with rescued wildlife silhouettes |
| App launcher icons | `app/src/main/res/mipmap-*/ic_launcher*.png` | Derived from `docs/assets/wild-haven-icon-source.png` | Project-owned generated asset | Yes | Yes | Density-specific Android launcher icons |
| Animal art placeholder (fox/deer/owl/lynx) | (procedural Compose Canvas in `WildlifeIllustration.kt`) | Self-coded | Owned | Yes | No | rabbit_001은 v0.4.0에서 PNG로 교체됨 (§2 참조). 나머지 4종은 후속 작업으로 같은 절차 적용 예정 |
| Font | TBD | TBD | TBD | TBD | TBD | Must verify before release |
| Sound effects | TBD | TBD | TBD | TBD | TBD | MVP can ship without sound |

---

## 2. AI 생성 에셋 (gen_meta 포함)

`ART_DIRECTION.md` §10 절차로 도입된 에셋은 아래 표에 한 줄씩 기록한다.
`gen_meta` 컬럼은 추적성을 위해 *반드시* 모든 슬롯을 채운다.

| Asset | File (in-app) | File (source) | Service | Model/Version | Prompt 요약 또는 SHA-256 prefix | Seed/Reference | Generated At | Modified | License Class |
|---|---|---|---|---|---|---|---|---|---|
| Cottontail rabbit (rabbit_001) card | `app/src/main/res/drawable-nodpi/wh_animal_rabbit_001.png` | `docs/assets/source/wh_animal_rabbit_001.png` (= bakeoff/rabbit_001/dalle3-1.png) | OpenAI ChatGPT Plus | DALL·E 3 / gpt-image-1 (2026-05-14) | `docs/assets/prompts/wh_animal_rabbit_001.txt` (SHA-256 prefix `1cc39e7fea5a`) | None (DALL·E 3는 시드/Reference 미노출 — 동일 프롬프트로 재현) | 2026-05-14 | 1254×1254 → 512×512 LANCZOS 리사이즈, PIL 256-color quantize (218 KB) | Project-owned (OpenAI gpt-image-1) |

### 2.1 라이선스 분류 정의

- **Project-owned (Firefly commercial-safe)** — Adobe Firefly 출력, Adobe 약관에
  따라 상업 사용 안전. 학습 데이터가 라이선스 정리됨.
- **Project-owned (OpenAI gpt-image-1)** — OpenAI 약관에 따라 출력 권리가 사용자
  소유. 상업 사용 가능. (학습 데이터 출처는 OpenAI 비공개 → IP 충돌 위험은 사람
  검수로 1차 차단.)
- **Project-owned (Imagen on Vertex)** — GCP Vertex AI 약관에 따라 상업 사용 가능.

### 2.2 프롬프트 기록 방법

- 짧은 프롬프트는 큰따옴표로 그대로 기록.
- 긴 프롬프트는 `docs/assets/prompts/<asset_name>.txt`로 별도 저장하고, 표에는
  파일의 SHA-256 앞 6자리를 기록 (`a3f8c1…`처럼).
- 프롬프트에는 IP/윤리 정책의 네거티브 슬롯이 포함돼 있어야 한다 (`ART_DIRECTION.md` §8.4).

---

## 3. 원칙

- 출처가 불명확한 에셋은 사용하지 않는다.
- 검색 이미지, 타 게임 스크린샷, 타사 캐릭터 이미지는 사용하지 않는다.
- 상업적 사용 가능 여부가 명확하지 않으면 사용하지 않는다.
- 유료 에셋은 구매 영수증 또는 라이선스 문구를 별도 보관한다.
- AI 생성 에셋은 §2의 모든 메타데이터 슬롯을 채워야 게임 번들에 포함될 수 있다.
- 한 번에 *하나의 AI 서비스*만 사용한다 (혼용 금지 — `ART_DIRECTION.md` §8.1 참조).
