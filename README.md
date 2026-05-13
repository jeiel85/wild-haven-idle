# Wild Haven Idle

Wild Haven Idle is a local-first Android idle simulation game about restoring a small wildlife sanctuary, supporting rescued animals through recovery stages, and completing a wildlife archive.

## Project Goals

- Build a simple, ethical idle collection game.
- Avoid imitation of existing electronic pet games or third-party IP.
- Keep the MVP local-first with no login, no ads, no analytics, and no network dependency.
- Support AI-agent-based development with clear safety rails and documentation rules.

## Recommended Repository

```text
wild-haven-idle
```

## Android Application ID

```text
com.jeiel85.wildhavenidle
```

## MVP Features

- Care point generation
- Offline reward calculation
- Sanctuary upgrade
- Rescued animal recovery stages
- Wildlife archive
- Local save
- Basic settings screen
- Data reset option

## Non-Goals for MVP

- No login
- No cloud sync
- No ads
- No billing
- No analytics
- No external API
- No push notifications
- No real donation claims
- No third-party IP references

## Documentation

- `AGENTS.md`: AI coding agent rules
- `docs/GAME_DESIGN.md`: game design specification
- `docs/TECH_SPEC.md`: Android technical specification
- `docs/BALANCE.md`: balance formulas and starting values
- `docs/ART_DIRECTION.md`: visual identity and art safety rules
- `docs/IP_AND_ETHICS.md`: IP and ethical design policy
- `docs/ASSET_LICENSES.md`: asset license tracking
- `docs/ROADMAP.md`: phased development roadmap
- `docs/MIGRATION.md`: save-data migration policy

## Development

Initial recommended commands:

```bash
./gradlew test
./gradlew assembleDebug
```

CI is expected to run on GitHub Actions.

## IP and Ethics

Wild Haven Idle is a wildlife sanctuary restoration idle simulation. It is not an electronic pet clone and does not reference, imitate, or reproduce any existing virtual pet product or third-party IP.

See `docs/IP_AND_ETHICS.md` for the full policy.
