package io.gitlab.jfronny.motortunez.gui.widgets;

import io.gitlab.jfronny.motortunez.MotorTunez;
import io.gitlab.jfronny.motortunez.gui.PlaylistViewScreen;
import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import io.gitlab.jfronny.motortunez.util.SearchUtil;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;

public class SearchBar extends CustomWidget {
    @Override
    public void add(WTable parent, TunezScreen screen, GuiTheme theme) {
        WTextBox box = parent.add(theme.textBox("")).expandX().widget();
        parent.add(theme.button("Search")).widget().action = () -> SearchUtil.search(box.get(),
                (title, description) -> MotorTunez.toast(title, description, SystemToast.Type.PACK_COPY_FAILURE),
                playlist -> MinecraftClient.getInstance().openScreen(new PlaylistViewScreen(theme, playlist, screen)));
        super.add(parent, screen, theme);
    }
}
