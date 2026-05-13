# Session Handoff

## 2026-05-13

### Current branch

- Branch: `codex/release-signing-icon`
- Base: `main`
- PR: https://github.com/jeiel85/wild-haven-idle/pull/2
- Latest commit: `757bddf docs: GitHub README 개편`
- Working tree: clean at handoff

### Open PRs

1. PR #1: `codex/feat-datastore-game-loop`
   - URL: https://github.com/jeiel85/wild-haven-idle/pull/1
   - Scope: DataStore save/load, home ticking loop, offline reward dialog
   - CI: passed
   - Note: this branch is separate from PR #2.

2. PR #2: `codex/release-signing-icon`
   - URL: https://github.com/jeiel85/wild-haven-idle/pull/2
   - Scope: release signing, signed APK/AAB CI artifact, app icon, README rewrite, Node 24 Actions update
   - CI: passed
   - Artifact: `wild-haven-idle-release`

### Release signing backup

Release signing files were generated outside the repository and must stay private.

```text
D:\Project\wild-haven-idle-release-backup\20260513-132122
```

Important files:

```text
wild-haven-release.jks
release-signing.env
wild-haven-release.jks.base64.txt
```

GitHub Actions Secrets registered:

```text
ANDROID_RELEASE_KEYSTORE_BASE64
ANDROID_RELEASE_KEYSTORE_PASSWORD
ANDROID_RELEASE_KEY_ALIAS
ANDROID_RELEASE_KEY_PASSWORD
```

### Completed in this session

- Created Android release keystore outside Git.
- Registered release signing secrets in GitHub.
- Added signed release APK/AAB build to Android CI.
- Added manual/tag-based Android Release workflow.
- Generated and wired the app launcher icon.
- Updated GitHub Actions to newer Node 24-compatible action versions.
- Rewrote `README.md` for GitHub project presentation.
- Verified CI after the latest README commit.

### Suggested next steps

1. Decide whether to merge PR #1 first, then rebase or merge PR #2 after it.
2. After PR #1 is merged, continue MVP gameplay work:
   - animal recovery list
   - sanctuary upgrade action
   - archive screen
   - settings screen
   - data reset action
3. After core MVP screens exist, create final Play Store listing assets:
   - real screenshots
   - feature graphic
   - short and long descriptions
   - data safety / privacy policy materials

### Cautions

- Do not commit files from `D:\Project\wild-haven-idle-release-backup`.
- Do not print or paste release keystore passwords into chat, logs, or docs.
- MVP policy still excludes network permission, login, ads, analytics, billing, cloud sync, and push notifications.
