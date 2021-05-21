package io.gitlab.jfronny.motortunez.hud;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.gitlab.jfronny.motortunez.MotorTunez;
import minegame159.meteorclient.rendering.DrawMode;
import minegame159.meteorclient.rendering.MeshBuilder;
import minegame159.meteorclient.rendering.Renderer;
import minegame159.meteorclient.rendering.text.TextRenderer;
import minegame159.meteorclient.settings.*;
import minegame159.meteorclient.systems.modules.render.hud.HUD;
import minegame159.meteorclient.systems.modules.render.hud.HudRenderer;
import minegame159.meteorclient.systems.modules.render.hud.modules.HudElement;
import minegame159.meteorclient.utils.render.color.SettingColor;
import net.minecraft.client.render.VertexFormats;

public class TunezHud extends HudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> showTitle = sgGeneral.add(new BoolSetting.Builder()
            .name("show-title")
            .description("Shows the title of the current song")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> showAuthor = sgGeneral.add(new BoolSetting.Builder()
            .name("show-author")
            .description("Shows the author of the current song")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> showTotalTime = sgGeneral.add(new BoolSetting.Builder()
            .name("show-total-time")
            .description("Shows the total amount of time left")
            .defaultValue(false)
            .build()
    );

    private final Setting<SettingColor> backgroundColor = sgGeneral.add(new ColorSetting.Builder()
            .name("background-color")
            .description("Color of background.")
            .defaultValue(new SettingColor(0, 0, 0, 64))
            .build()
    );

    private final Setting<SettingColor> progressColor = sgGeneral.add(new ColorSetting.Builder()
            .name("progress-color")
            .description("Color of the progress bar.")
            .defaultValue(new SettingColor(120, 43, 153))
            .build()
    );

    private final Setting<SettingColor> progressBackgroundColor = sgGeneral.add(new ColorSetting.Builder()
            .name("progress-background-color")
            .description("Color of the progress bars background.")
            .defaultValue(new SettingColor(35, 35, 35, 100))
            .build()
    );

    private final Setting<SettingColor> textColor = sgGeneral.add(new ColorSetting.Builder()
            .name("text-color")
            .description("Color of text and controls.")
            .defaultValue(new SettingColor(255, 255, 255))
            .build()
    );

    private final Setting<Double> height = sgGeneral.add(new DoubleSetting.Builder()
            .name("height")
            .description("The height of this element")
            .defaultValue(100)
            .min(20)
            .sliderMin(20)
            .sliderMax(200)
            .build()
    );

    private final Setting<Double> recordPart = sgGeneral.add(new DoubleSetting.Builder()
            .name("record-part")
            .description("The relative amount of space the progress bar should consume")
            .defaultValue(0.8)
            .min(0.1)
            .sliderMin(0.1)
            .sliderMax(1)
            .build()
    );

    private final Setting<Double> progressWidth = sgGeneral.add(new DoubleSetting.Builder()
            .name("progress-width")
            .description("The relative amount of pixels to use in the actual progress part of the bar")
            .defaultValue(5)
            .min(1)
            .sliderMin(1)
            .sliderMax(10)
            .build()
    );

    private final Setting<Double> recordStatusPart = sgGeneral.add(new DoubleSetting.Builder()
            .name("record-status-part")
            .description("The relative amount of space the status component of the progress bar should consume")
            .defaultValue(0.5)
            .min(0.1)
            .sliderMin(0.1)
            .sliderMax(1)
            .build()
    );

    private final Setting<Double> textScale = sgGeneral.add(new DoubleSetting.Builder()
            .name("text-scale")
            .description("The relative amount of space the status component of the progress bar should consume")
            .defaultValue(3)
            .min(1)
            .sliderMin(1)
            .sliderMax(5)
            .build()
    );
    
    public TunezHud(HUD hud) {
        super(hud, "tunez", "Displays the current music");
    }

    private static final double circle = Math.PI * 2;
    private static final double headerFactor = 0.45;
    private static final double footerFactor = 0.2;
    private static final String notPlaying = "Not playing";

    @Override
    public void update(HudRenderer renderer) {
        double width = height.get();
        if (MotorTunez.player != null) {
            AudioTrack current = MotorTunez.player.getPlayingTrack();
            TextRenderer tr = TextRenderer.get();
            tr.end();
            tr.begin(headerFactor * textScale.get(), false, true);
            if (showTitle.get()) {
                width = Math.max(width, height.get() + renderer.roundAmount() + tr.getWidth(current == null ? notPlaying : current.getInfo().title));
            }
            if (showAuthor.get()) {
                width = Math.max(width, height.get() + renderer.roundAmount() + tr.getWidth(current == null ? notPlaying : current.getInfo().author));
            }
            tr.end();
            tr.begin();
        }
        box.setSize(width, height.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        renderer.addPostTask(() -> {
            double x = box.getX();
            double y = box.getY();
            double w = box.width;
            double h = box.height;
            MeshBuilder mb = Renderer.NORMAL;

            // Background
            mb.begin(null, DrawMode.Triangles, VertexFormats.POSITION_COLOR);
            mb.quadRounded(x, y, w, h, backgroundColor.get(), renderer.roundAmount(), true);

            AudioTrack current = MotorTunez.player.getPlayingTrack();
            double progress = current == null ? 1 : current.getPosition() * 1d / current.getDuration();
            String title = current == null ? notPlaying : current.getInfo().title;
            String author = current == null ? notPlaying : current.getInfo().author;

            // Song view
            double r = h * recordPart.get() / 2;
            double start = h / 2;
            mb.circlePart(x + start, y + start, r, 0, circle, progressBackgroundColor.get());
            mb.circlePartOutline(x + start, y + start, r, 0, circle * progress, progressColor.get(), progressWidth.get());

            double controlSizeHalf = r * recordStatusPart.get();
            if (MotorTunez.player.isPaused()) {
                double pauseStart = h / 2 - controlSizeHalf;
                double quadWidth = controlSizeHalf * 2 / 3;
                mb.quad(x + pauseStart, y + pauseStart, quadWidth, controlSizeHalf * 2, textColor.get());
                mb.quad(x + h / 2 + controlSizeHalf - quadWidth, y + pauseStart, quadWidth, controlSizeHalf * 2, textColor.get());
            }
            else {
                controlSizeHalf /= 2;
                double startX = h / 2 - controlSizeHalf;
                double startY = h / 2 - controlSizeHalf * 2;
                mb.vert2(x + startX, y + startY, textColor.get());
                mb.vert2(x + startX, y + startY + controlSizeHalf * 4, textColor.get());
                mb.vert2(x + startX + controlSizeHalf * 2, y + startY + controlSizeHalf * 2, textColor.get());
            }
            mb.end();

            TextRenderer tr = TextRenderer.get();
            tr.begin(headerFactor * textScale.get(), false, true);
            // Title
            if (showTitle.get()) {
                tr.render(title, x + h, y + h / 2 - tr.getHeight() / (showAuthor.get() ? 1 : 2), textColor.get(), true);
            }
            // Author
            if (showAuthor.get()) {
                tr.render(author, x + h, y + h / 2 - (showTitle.get() ? 0 : tr.getHeight() / 2), textColor.get(), true);
            }
            tr.end();
            // Total duration in minutes
            tr.begin(footerFactor * textScale.get(), false, true);
            if (showTotalTime.get()) {
                String t = MotorTunez.getTime();
                tr.render(t, x + r - tr.getWidth(t) / 2, y + h - tr.getHeight(), textColor.get());
            }
            tr.end();
        });
    }
}
