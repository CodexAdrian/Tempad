package me.codexadrian.tempad.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.client.widgets.TextButton;
import me.codexadrian.tempad.client.widgets.TimedoorSprite;
import me.codexadrian.tempad.tempad.LocationData;
import me.codexadrian.tempad.tempad.TempadComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RunProgramScreen extends Screen {
    private static final ResourceLocation GRID = new ResourceLocation(Tempad.MODID, "textures/widget/tempad_grid.png");
    private final int color;

    private static final int WIDTH = 480;
    private static final int HEIGHT = 256;
    private int mouseMovement;
    private int listSize;
    private final InteractionHand hand;
    private boolean interfaceNeedsReload = false;
    private boolean listNeedsReload = false;
    private final TimedoorSprite timedoorSprite;
    private List<LocationData> allLocations;
    private List<TextButton> displayedLocations;
    private final List<Button> displayedInterfaceButtons = new ArrayList<>();
    private final List<Button> upNextButtons = new ArrayList<>();

    public RunProgramScreen(int color, InteractionHand hand) {
        super(Component.nullToEmpty(""));
        this.color = color;
        this.hand = hand;
        //displayedLocation = new TextButton(0, 0, 12, Component.nullToEmpty(""), color, true, (but)->{});
        //teleportButton = new TextButton(0, 0, 12, Component.nullToEmpty(""), color, true, (but)->{});
        //deleteLocationButton = new TextButton(0,0, 12, Component.nullToEmpty(""), color, true, (but)->{});
        timedoorSprite = new TimedoorSprite(0, 0, color, 16 * 9);
        allLocations = new ArrayList<>();
        displayedLocations = new ArrayList<>();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int wiggleRoom = listSize > 12 ? listSize - 12 : 0;
        mouseMovement = Math.min(wiggleRoom, Math.max(mouseMovement + (int) (delta * 10), 0));
        if(listSize > 12) listNeedsReload = true;
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        super.init();
        int offset = 3;
        timedoorSprite.changePosition((width - WIDTH) / 2 + 16 * 3, (height - HEIGHT) / 2 + offset + 16 * 2);
        ItemStack stack = minecraft.player.getItemInHand(this.hand);
        TextButton debugButton = new TextButton((width - WIDTH) / 2 + offset + 16 * 17, (height - HEIGHT) / 2 + offset + 16 * 2, 16, new TextComponent("Temp"), color, (but) -> {
            String nameFieldText = LocalDateTime.now().toString();
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeInt(nameFieldText.length());
            buf.writeCharSequence(nameFieldText, StandardCharsets.UTF_8);
            buf.writeEnum(hand);
            ClientPlayNetworking.send(Tempad.LOCATION_PACKET, buf);
            Minecraft.getInstance().setScreen(null);
            //root.remove(addLocation);
            //Minecraft.getInstance().setScreen(new TempadInterfaceGui(new RunProgramScreenDesc(false, null, hand, player, color)));
        });
        //addRenderableWidget(debugButton);
        if(stack.hasTag()) {
            allLocations = new ArrayList<>(TempadComponent.fromStack(stack).getLocations());
            listSize = allLocations.size();
            List<LocationData> shownLocationData = new ArrayList<>();
            if(listSize > 12) {
                for(int i = 0; i < 12; i++) {
                    shownLocationData.add(allLocations.get(i));
                    System.out.println(i);
                }
            } else {
                shownLocationData = allLocations;
            }

            int x = (width - WIDTH) / 2 + offset + 16 * 15;
            int y = (height - HEIGHT) / 2 + offset + 16 * 2;
            for(LocationData data : shownLocationData) {
                var locationButton = new TextButton(x, y, 12, new TextComponent(data.getName()), color, (button) -> locationButtonOnPress(data));
                this.displayedLocations.add(locationButton);
                addRenderableWidget(locationButton);
                y+=16;
            }
        }

        //addRenderableWidget(new TimedoorSprite((width - WIDTH) / 2 + offset + 16 * 5, (height - HEIGHT) / 2 + offset + 16 * 2, color, 128));

    }

    private void locationButtonOnPress(LocationData data) {
        String locationName = data.getBlockPos().toShortString();
        TextButton displayedLocation = new TextButton((width - WIDTH) / 2 + 16 * 8 - (int)(font.width(locationName) * .75) - 8, (height - HEIGHT) / 2 + 3 + 16 * 11,12, new TextComponent(locationName), color, (button1) -> {});
        var teleportText = new TranslatableComponent("gui." + Tempad.MODID + ".teleport");
        TextButton teleportButton = new TextButton((width - WIDTH) / 2 + 16 * 8 - (int)(font.width(teleportText) * .75) - 8, (height - HEIGHT) / 2 + 3 + 16 * 12,12, teleportText, color, (button2) ->{
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeResourceLocation(data.getLevelKey().location());
            buf.writeBlockPos(data.getBlockPos());
            buf.writeEnum(hand);
            Minecraft.getInstance().setScreen(null);
            ClientPlayNetworking.send(Tempad.TIMEDOOR_PACKET, buf);
        });
        var deleteText = new TranslatableComponent("gui." + Tempad.MODID + ".delete");
        TextButton deleteLocationButton = new TextButton((width - WIDTH) / 2 + 16 * 8 - (int)(font.width(deleteText) * .75) - 8, (height - HEIGHT) / 2 + 3 + 16 * 13,12, deleteText, color, (button2) ->{
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeEnum(hand);
            buf.writeUUID(data.getId());
            Minecraft.getInstance().setScreen(null);
            ClientPlayNetworking.send(Tempad.DELETE_LOCATION_PACKET, buf);
        });

        upNextButtons.add(displayedLocation);
        upNextButtons.add(teleportButton);
        upNextButtons.add(deleteLocationButton);
        this.interfaceNeedsReload = true;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.interfaceNeedsReload) {
            displayedInterfaceButtons.forEach(this::removeWidget);
            displayedInterfaceButtons.clear();
            displayedInterfaceButtons.addAll(upNextButtons);
            upNextButtons.clear();
            displayedInterfaceButtons.forEach(this::addRenderableWidget);
            removeWidget(timedoorSprite);
            addRenderableWidget(timedoorSprite);
            this.interfaceNeedsReload = false;
        }
        int x = (width - WIDTH) / 2 + 3 + 16 * 15;
        int y = (height - HEIGHT) / 2 + 3 + 16 * 2;
        if(this.listNeedsReload) {

            for(TextButton button : displayedLocations) {
                removeWidget(button);
            }
            displayedLocations = new ArrayList<>();
            int offset = Math.min(mouseMovement, listSize - 12);
            for(int i = offset; i < 12 + offset; i++) {
                LocationData data = allLocations.get(i);
                displayedLocations.add(new TextButton(x, y, 12, new TextComponent(data.getName()), color, (button -> locationButtonOnPress(data))));
                y+=16;
            }
            displayedLocations.forEach(this::addRenderableWidget);
            this.listNeedsReload = false;
        }
    }

    private void renderOutline(PoseStack poseStack) {
        int lineWidth = 4;
        fill(poseStack, (width - WIDTH - lineWidth) / 2, (height - HEIGHT - lineWidth) / 2, (width + WIDTH + lineWidth) / 2, (height + HEIGHT + lineWidth) / 2, color | 0xFF000000);
    }

    private void renderGridBackground(PoseStack poseStack, float red, float green, float blue) {
        RenderSystem.setShaderTexture(0, GRID);
        RenderSystem.setShaderColor(red * 0.5f, green * 0.5f, blue * 0.5f, 1f);
        blit(poseStack, (width - WIDTH) / 2, (height - HEIGHT) / 2, WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT, 16, 16);
    }

    @Override
    public void renderBackground(PoseStack poseStack, int offset) {
        super.renderBackground(poseStack, offset);
        float red = (color >> 16 & 0xFF) / 255f;
        float green = (color >> 8 & 0xFF) / 255f;
        float blue = (color & 0xFF) / 255f;
        renderOutline(poseStack);
        renderGridBackground(poseStack, red, green, blue);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
