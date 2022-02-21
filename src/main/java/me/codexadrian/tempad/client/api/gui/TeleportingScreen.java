/*
package me.codexadrian.tempad.client.api.gui;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.background.EmptyBackground;
import dev.lambdaurora.spruceui.widget.SpruceLabelWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import dev.lambdaurora.spruceui.widget.container.tabbed.SpruceTabbedWidget;
import me.codexadrian.tempad.client.widgets.SpruceIconWidget;
import me.codexadrian.tempad.tempad.TempadComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static me.codexadrian.tempad.Tempad.MODID;

public class TeleportingScreen extends BaseTempadScreen {
    public static final ResourceLocation TIMEDOOR_SPRITE = new ResourceLocation(MODID, "textures/widget/timedoor_sprite.png");
    public TeleportingScreen(int color, Player player, InteractionHand hand) {
        super(color, player, hand, new TranslatableComponent(MODID, "gui.teleporting_screen.title"));
    }

    @Override
    protected void init() {
        super.init();
        */
/*
        SpruceIconWidget doorIcon1 = new SpruceIconWidget(Position.of(this.width / 2 - PANEL_WIDTH / 2, height / 2 - PANEL_HEIGHT / 2), TIMEDOOR_SPRITE, 32, 32);
        addWidget(doorIcon1);
        SpruceTabbedWidget tabbedWidget = new SpruceTabbedWidget(Position.of(this.width / 2 - PANEL_WIDTH / 2, height / 2 - PANEL_HEIGHT / 2), PANEL_WIDTH, PANEL_HEIGHT, this.title);
        tabbedWidget.getList().setBackground(EmptyBackground.EMPTY_BACKGROUND);
        ItemStack stack = player.getItemInHand(hand);
        tabbedWidget.addTabEntry(new TextComponent("Home"), null, (width, height) -> {
            var container = new SpruceContainerWidget(Position.origin(), width, height);
            container.addChildren((containerWidth, containerHeight, widgetAdder) -> {
                widgetAdder.accept(new SpruceIconWidget(Position.of(containerWidth/2 - 16, 48), TIMEDOOR_SPRITE, 32, 32));
                widgetAdder.accept(new SpruceLabelWidget(Position.of(0, 16), new TextComponent("Hello World!").withStyle(ChatFormatting.WHITE), containerWidth, true));
                widgetAdder.accept(new SpruceLabelWidget(Position.of(0, 48), new TextComponent("This is a tabbed widget. You can switch tabs by using the list on the left.\n It also allows quite a good controller support and arrow key navigation.").withStyle(ChatFormatting.WHITE), containerWidth, true));
            });
            return container;
        });
        tabbedWidget.addSeparatorEntry(new TextComponent("Locations"));
        if (stack.hasTag()) {
            TempadComponent component = TempadComponent.fromStack(stack);
            component.getLocations().forEach(data -> tabbedWidget.addTabEntry(new TextComponent(data.getName()), null, (width1, height1) -> {
                var container = new SpruceContainerWidget(Position.origin(), width1, height1);
                SpruceLabelWidget test = new SpruceLabelWidget(Position.of(width1/2 - 16, 48), new TextComponent("This is a tabbed widget. You can switch tabs by using the list on the left.\n It also allows quite a good controller support and arrow key navigation.").withStyle(ChatFormatting.WHITE), width1, true);
                SpruceIconWidget doorIcon = new SpruceIconWidget(Position.of(width1/2 - 16, 48), TIMEDOOR_SPRITE, 32, 32);
                doorIcon.setTint(color);
                container.addChild(doorIcon);
                container.addChild(test);
                return container;
            }));
        }
        //addWidget(tabbedWidget);

         *//*

    }
}
*/
