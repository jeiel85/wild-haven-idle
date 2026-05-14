"""Resize the source icon to Play Store 512x512 spec."""
from pathlib import Path
from PIL import Image

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "docs" / "assets" / "wild-haven-icon-source.png"
DST = ROOT / "store-graphics" / "icon-512.png"

img = Image.open(SRC).convert("RGBA")
resized = img.resize((512, 512), Image.LANCZOS)
resized.save(DST, "PNG", optimize=True)

size_kb = DST.stat().st_size / 1024
print(f"Wrote {DST.relative_to(ROOT)} ({resized.size[0]}x{resized.size[1]}, {size_kb:.1f} KB)")
