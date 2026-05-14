"""Crop the status bar (top 100 px) from captured screenshots.

The Samsung One UI status bar shows a live-activity pill (sports widget) that
should not appear in store listing screenshots. Rather than masking the widget
in place, we crop the entire status bar so the screenshot starts with the app
content. The resulting 1080x2240 image remains inside Play Store's 320-3840 px
side-length requirement and aspect ratio allowances.
"""
from pathlib import Path
from PIL import Image

ROOT = Path(__file__).resolve().parent
SCREENS = ROOT / "screenshots"

STATUS_BAR_H = 100  # pixels

for png in sorted(SCREENS.glob("*.png")):
    img = Image.open(png).convert("RGB")
    w, h = img.size
    cropped = img.crop((0, STATUS_BAR_H, w, h))
    cropped.save(png, "PNG", optimize=True)
    print(f"  cropped {png.name}  {w}x{h} -> {cropped.size[0]}x{cropped.size[1]}")

print("Done.")
