package io.gitlab.jfronny.motortunez.gui;

import com.ibm.icu.text.UTF16;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import io.gitlab.jfronny.motortunez.MotorTunez;
import io.gitlab.jfronny.motortunez.gui.widgets.CustomWidget;
import io.gitlab.jfronny.motortunez.gui.widgets.PaginationProvider;
import io.gitlab.jfronny.motortunez.util.PlaylistUtil;
import io.gitlab.jfronny.motortunez.util.SearchUtil;
import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.WindowScreen;
import minegame159.meteorclient.gui.widgets.containers.WTable;
import minegame159.meteorclient.gui.widgets.input.WTextBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;

import java.util.*;

public class PlaylistsScreen extends WindowScreen {
    private final TunezScreen tunezScreen;
    private List<CustomWidget> childWidgets;
    private WTable table;
    private PaginationProvider pagination;

    public PlaylistsScreen(GuiTheme theme, TunezScreen tunezScreen) {
        super(theme, "Music - Playlists");
        this.tunezScreen = tunezScreen;
        this.parent = tunezScreen;
    }

    @Override
    protected void init() {
        super.init();
        childWidgets = new ArrayList<>();

        pagination = new PaginationProvider(j -> construct());
        childWidgets.add(pagination);

        if (table != null) table.clear();
        clear();
        table = add(theme.table()).expandX().minWidth(300).widget();
        construct();
    }

    public void construct() {
        pagination.setMaxPage(PlaylistUtil.count() / TunezScreen.pageSize);
        table.clear();

        WTextBox box = table.add(theme.textBox("")).expandX().widget();
        table.add(theme.plus()).widget().action = () -> SearchUtil.search(box.get(),
                (title, description) -> MotorTunez.toast(title, description, SystemToast.Type.PACK_COPY_FAILURE),
                playlist -> {
                    PlaylistUtil.add(box.get());
                    construct();
                });

        List<Map.Entry<String, AudioPlaylist>> keys = PlaylistUtil.getEntriesOrdered();
        for (int i = 0; i < keys.size(); i++) {
            int j = i + pagination.getPageOffset();
            table.row();
            AudioPlaylist playlist = keys.get(j).getValue();

            table.add(theme.button(playlist.getName())).expandX().widget().action =() ->
                    MinecraftClient.getInstance().openScreen(new PlaylistViewScreen(theme, playlist, tunezScreen).setParent(this));

            table.add(theme.minus()).right().widget().action = () -> {
                PlaylistUtil.remove(keys.get(j).getKey());
                construct();
            };
        }

        for (CustomWidget customWidget : childWidgets) {
            customWidget.add(table, tunezScreen, theme);
        }
    }
}