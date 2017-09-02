package extracells.gui.widget;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import extracells.gui.GuiBase;

@SideOnly(Side.CLIENT)
public class WidgetManager {
	public final GuiBase gui;
	public final Minecraft mc;
	protected final List<AbstractWidget> widgets = new ArrayList<>();

	public WidgetManager(GuiBase gui) {
		this.gui = gui;
		this.mc = Minecraft.getMinecraft();
	}

	public void add(AbstractWidget widget) {
		widgets.add(widget);
	}

	public void remove(AbstractWidget slot) {
		this.widgets.remove(slot);
	}

	public void clear() {
		this.widgets.clear();
	}

	public List<AbstractWidget> getWidgets() {
		return widgets;
	}

	public void drawWidgets(int mouseX, int mouseY) {
		for (AbstractWidget widget : widgets) {
			widget.draw(mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop());
		}
	}

	public void drawToolTips(int mouseX, int mouseY) {
		AbstractWidget slot = getAtPosition(mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop());
		if (slot != null) {
			List<String> lines = slot.getToolTip(mouseX, mouseY);
			if (!lines.isEmpty()) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(-gui.getGuiLeft(), -gui.getGuiTop(), 0);
				ScaledResolution scaledresolution = new ScaledResolution(mc);
				GuiUtils.drawHoveringText(lines, mouseX, mouseY, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 1, mc.fontRendererObj);
				GlStateManager.popMatrix();
			}
		}
	}

	@Nullable
	public AbstractWidget getAtPosition(int mX, int mY) {
		for (AbstractWidget slot : widgets) {
			if (slot.isMouseOver(mX, mY)) {
				return slot;
			}
		}
		return null;
	}

	public void handleMouseClicked(int mouseX, int mouseY, int mouseButton) {
		AbstractWidget slot = getAtPosition(mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop());
		if (slot != null) {
			slot.handleMouseClick(mouseX, mouseY, mouseButton);
		}
	}
}