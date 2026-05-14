# Tasks

## Now

- [ ] Create GitHub repository `wild-haven-idle`
- [x] Create Android Compose project
- [x] Set applicationId to `com.jeiel85.wildhavenidle`
- [x] Add domain models
- [x] Add BalanceCalculator
- [x] Add unit tests for BalanceCalculator
- [x] Add simple HomeScreen MVP
- [x] Create release keystore backup outside repository
- [x] Configure signed release APK/AAB builds
- [x] Add GitHub Actions release workflow using repository secrets
- [x] Add game identity app icon
- [x] Publish GitHub Release with signed APK/AAB on tag push
- [x] Surface APK download CTA on GitHub Pages

## Next

- [x] Install/configure Android SDK locally or verify GitHub Actions build environment
- [x] Add DataStore save/load
- [x] Add offline reward dialog
- [x] Add animal recovery list
- [x] Add archive screen
- [x] Add settings screen
- [x] Add data reset action
- [x] Add navigation with Compose Navigation

## Later

- [ ] Add habitat expansion
- [ ] Add self-made art assets
- [ ] Add localization resources for Korean and English
- [ ] Add optional reward ads only after MVP validation
- [ ] Add cloud sync only after separate approval

## Commercialization Phase 0.5 — Design Polish (per docs/COMMERCIALIZATION_PLAN.md §0.5)

- [x] Pin asset pipeline (AI service selection rules, prompt template, license logging) in `docs/ART_DIRECTION.md` §8~§10
- [x] P1: Tokenize visual system in `WildHavenTheme` (color/typography/spacing/radius/elevation)
- [x] Decide AI image service — **locked: OpenAI gpt-image-1 / DALL·E 3 (ChatGPT Plus)** after rabbit_001 bake-off (2026-05-14)
- [x] P2: Replace sanctuary header with one AI-generated landscape PNG (`wh_habitat_forest_001.png`, 960×540, 395 KB) — SanctuaryHeader의 절차적 풍경 캔버스 제거, 동물 스프라이트/Floater/Lv 라벨 유지
- [x] P3: Generate 5 animal illustrations and switch `AnimalIllustration` to PNG
  - [x] `wh_animal_rabbit_001.png` (dalle3-1, 512×512 quantized 218 KB)
  - [x] `wh_animal_fox_001.png` (211 KB)
  - [x] `wh_animal_deer_001.png` (212 KB)
  - [x] `wh_animal_owl_001.png` (191 KB)
  - [x] `wh_animal_lynx_001.png` (219 KB)
  - [x] `AnimalIllustration` map switch: 5종 모두 PNG, `color` 파라미터 제거 (절차적 호출 폐기)
  - [x] `AnimalSilhouette` (LOCKED) 절차적 Canvas 유지 — 그레이 통일감 위해 의도적 보존
- [ ] P4: Card/button micro-polish (shadow, radius, spacing, progress typography)
- [ ] P5: Motion timing/easing tokens unified

## Commercialization Phase 1 (per docs/COMMERCIALIZATION_PLAN.md)

- [x] First-run onboarding overlay (3-step intro on Home, shown only for fresh data)
- [ ] Highlight a single recommended next-action card on Home
- [ ] Tighten care point / production readability on small screens

## Commercialization Phase 2

- [ ] Local daily login bonus (no ads, no network)
- [ ] First-recovery milestone celebration dialog (per animal, once)
- [ ] Offline reward dialog: append a 1-line next-step suggestion

## Commercialization Phase 3

- [ ] Daily missions (3 rolling, local) with progress and reward
- [ ] Archive progress card on Home
- [ ] Sanctuary milestone rewards (Lv.5/10/20 once)
