"""Generate the Play Store feature graphic (1024x500) for Wild Haven Idle.

Uses the project's branding palette (forest/leaf/moss/sun/paper) so the asset
matches docs/index.html and the in-app theme.
"""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFilter, ImageFont

ROOT = Path(__file__).resolve().parent.parent
ICON_SRC = ROOT / "docs" / "assets" / "wild-haven-icon-source.png"
DST = ROOT / "store-graphics" / "feature-graphic-1024x500.png"

W, H = 1024, 500

FOREST = (31, 60, 44)
DEEP = (15, 36, 25)
LEAF = (79, 138, 91)
MOSS = (223, 234, 209)
SUN = (244, 201, 107)
PAPER = (255, 250, 240)
INK = (23, 35, 27)


def vertical_gradient(size, top, bottom):
    img = Image.new("RGB", size, top)
    px = img.load()
    w, h = size
    for y in range(h):
        t = y / (h - 1)
        r = round(top[0] * (1 - t) + bottom[0] * t)
        g = round(top[1] * (1 - t) + bottom[1] * t)
        b = round(top[2] * (1 - t) + bottom[2] * t)
        for x in range(w):
            px[x, y] = (r, g, b)
    return img


def radial_glow(size, center, radius, color, max_alpha=120):
    layer = Image.new("RGBA", size, (0, 0, 0, 0))
    draw = ImageDraw.Draw(layer)
    cx, cy = center
    draw.ellipse(
        [cx - radius, cy - radius, cx + radius, cy + radius],
        fill=(*color, max_alpha),
    )
    return layer.filter(ImageFilter.GaussianBlur(radius * 0.55))


def load_font(size, weight="bold"):
    candidates = []
    if weight == "bold":
        candidates += [
            "C:/Windows/Fonts/malgunbd.ttf",
            "C:/Windows/Fonts/arialbd.ttf",
        ]
    else:
        candidates += [
            "C:/Windows/Fonts/malgun.ttf",
            "C:/Windows/Fonts/arial.ttf",
        ]
    for path in candidates:
        if Path(path).exists():
            return ImageFont.truetype(path, size)
    return ImageFont.load_default()


canvas = vertical_gradient((W, H), DEEP, FOREST).convert("RGBA")

# Subtle warm glow upper-left (sun) and cool glow lower-right (water/leaf accent).
canvas.alpha_composite(radial_glow((W, H), (-40, 60), 360, SUN, 90))
canvas.alpha_composite(radial_glow((W, H), (W + 80, H + 60), 420, LEAF, 110))

# Decorative leaf-like dots scattered on the right side as texture.
texture = Image.new("RGBA", (W, H), (0, 0, 0, 0))
tdraw = ImageDraw.Draw(texture)
for cx, cy, r, a in [
    (760, 90, 6, 110),
    (820, 140, 4, 80),
    (700, 200, 5, 90),
    (880, 260, 7, 100),
    (740, 360, 5, 70),
    (940, 110, 4, 70),
    (820, 420, 6, 90),
]:
    tdraw.ellipse([cx - r, cy - r, cx + r, cy + r], fill=(*MOSS, a))
canvas.alpha_composite(texture.filter(ImageFilter.GaussianBlur(1.6)))

draw = ImageDraw.Draw(canvas)

# Eyebrow label.
eyebrow_font = load_font(20, "bold")
eyebrow = "WILDLIFE SANCTUARY IDLE"
draw.text((70, 78), eyebrow, font=eyebrow_font, fill=(*SUN, 255))
# Small dot in front of eyebrow.
draw.ellipse([46, 88, 60, 102], fill=SUN)

# Title (English).
title_font = load_font(62, "bold")
draw.text((70, 116), "Wild Haven Idle", font=title_font, fill=PAPER)

# Subtitle (Korean).
sub_font = load_font(26, "bold")
draw.text((70, 198), "와일드 헤이븐 — 보호구역 시뮬", font=sub_font, fill=(*MOSS, 255))

# Tagline.
tag_font = load_font(20, "regular")
draw.text(
    (70, 248),
    "작은 보호구역에서 시작하는",
    font=tag_font,
    fill=(*MOSS, 230),
)
draw.text(
    (70, 278),
    "야생동물 회복과 도감 수집 시뮬레이션",
    font=tag_font,
    fill=(*MOSS, 230),
)

# Feature chips at bottom of text column.
chip_font = load_font(17, "bold")
chips = ["오프라인 보상", "구조 · 회복", "도감", "광고·결제 없음"]
chip_x = 70
chip_y = 372
chip_h = 38
pad_x = 14
gap = 10
for label in chips:
    bbox = draw.textbbox((0, 0), label, font=chip_font)
    tw = bbox[2] - bbox[0]
    chip_w = tw + pad_x * 2
    chip_rect = [chip_x, chip_y, chip_x + chip_w, chip_y + chip_h]
    draw.rounded_rectangle(chip_rect, radius=19, fill=(0, 0, 0, 90), outline=(*MOSS, 210), width=1)
    text_y = chip_y + (chip_h - bbox[3]) // 2 - 1
    draw.text((chip_x + pad_x, text_y), label, font=chip_font, fill=(*MOSS, 255))
    chip_x += chip_w + gap

# Icon panel on the right.
icon_box_size = 300
icon_x = W - icon_box_size - 56
icon_y = (H - icon_box_size) // 2

# Soft shadow under the icon panel.
shadow = Image.new("RGBA", (W, H), (0, 0, 0, 0))
sdraw = ImageDraw.Draw(shadow)
sdraw.rounded_rectangle(
    [icon_x + 14, icon_y + 22, icon_x + icon_box_size + 14, icon_y + icon_box_size + 22],
    radius=44,
    fill=(0, 0, 0, 120),
)
canvas.alpha_composite(shadow.filter(ImageFilter.GaussianBlur(18)))

# Icon panel background.
panel = Image.new("RGBA", (icon_box_size, icon_box_size), (0, 0, 0, 0))
pdraw = ImageDraw.Draw(panel)
pdraw.rounded_rectangle(
    [0, 0, icon_box_size, icon_box_size],
    radius=44,
    fill=(*PAPER, 235),
    outline=(*MOSS, 220),
    width=2,
)
canvas.alpha_composite(panel, (icon_x, icon_y))

# Icon image (rounded mask).
icon_pad = 22
icon_inner = icon_box_size - icon_pad * 2
icon_img = Image.open(ICON_SRC).convert("RGBA").resize((icon_inner, icon_inner), Image.LANCZOS)
mask = Image.new("L", (icon_inner, icon_inner), 0)
ImageDraw.Draw(mask).rounded_rectangle([0, 0, icon_inner, icon_inner], radius=30, fill=255)
icon_layer = Image.new("RGBA", (icon_inner, icon_inner), (0, 0, 0, 0))
icon_layer.paste(icon_img, (0, 0), mask)
canvas.alpha_composite(icon_layer, (icon_x + icon_pad, icon_y + icon_pad))

# Final flatten and save.
final = canvas.convert("RGB")
final.save(DST, "PNG", optimize=True)
size_kb = DST.stat().st_size / 1024
print(f"Wrote {DST.relative_to(ROOT)} ({W}x{H}, {size_kb:.1f} KB)")
