package me.linckode.pluginutils.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public class GUIView implements Listener {

    private final GUIHolder guiHolder;
    private final InventoryView inventoryView;
    private final GUIViewSettings settings;
    private final Player player;
    private final GUIViewEventCallbackHolder eventCallbackHolder;
    private final Map<Integer, GUIClickAction> actionMap;

    public GUIView(Player player, GUIHolder guiHolder, InventoryView inventoryView, Map<Integer, GUIClickAction> actionMap, GUIViewSettings settings, GUIViewEventCallbackHolder eventCallbackHolder) {
        this.player = player;
        this.guiHolder = guiHolder;
        this.inventoryView = inventoryView;
        this.actionMap = actionMap;
        this.settings = settings;
        this.eventCallbackHolder = eventCallbackHolder;

        Bukkit.getPluginManager().registerEvents(this, guiHolder.getPlugin());
    }

    public Inventory topInventory(){
        return inventoryView.getTopInventory();
    }

    public Inventory bottomInventory() {
        return inventoryView.getBottomInventory();
    }

    public void setTitle(String title){
        inventoryView.setTitle(title);
    }
    public String getTitle(){
        return inventoryView.getTitle();
    }

    public void setItemAtRawIndex(int index, ItemStack itemStack){
        getInventoryView().setItem(index, itemStack);
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != this.guiHolder) return;
        if (eventCallbackHolder.closeEventCallback != null) eventCallbackHolder.closeEventCallback.onEvent(event, this);
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //If not this handler return
        if (event.getInventory().getHolder() != this.guiHolder) return;

        event.setCancelled(true);

        if (eventCallbackHolder.clickEventCallback != null) eventCallbackHolder.clickEventCallback.onEvent(event, this);

        if (!actionMap.containsKey(event.getRawSlot())){
            if (settings.getAllowedClickTypes().contains(event.getClick())) event.setCancelled(false);
            return;
        }

        actionMap.get(event.getRawSlot()).onClick(event, this);

    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() != this.guiHolder) return;

        event.setCancelled(!settings.enableDrag);

        if (eventCallbackHolder.dragEventCallback != null) eventCallbackHolder.dragEventCallback.onEvent(event, this);
    }

    public static class GUIViewSettings {

        private final ArrayList<ClickType> allowedClickTypes = new ArrayList<>();
        private boolean enableDrag = false;

        public GUIViewSettings() {

        }

        public static GUIViewSettings defaults() {
            return new GUIViewSettings();
        }

        public boolean isEnableDrag() {
            return enableDrag;
        }

        public void setEnableDrag(boolean enableDrag) {
            this.enableDrag = enableDrag;
        }

        public ArrayList<ClickType> getAllowedClickTypes() {
            return allowedClickTypes;
        }
    }

    public static class GUIViewEventCallbackHolder {

        public interface GUIViewEventCallback<T extends InventoryEvent> {

            void onEvent(T inventoryEvent, GUIView view);

        }

        private GUIViewEventCallback<InventoryCloseEvent> closeEventCallback;
        private GUIViewEventCallback<InventoryClickEvent> clickEventCallback;
        private GUIViewEventCallback<InventoryDragEvent> dragEventCallback;

        public void setCloseEventCallback(GUIViewEventCallback<InventoryCloseEvent> closeEventCallback) {
            this.closeEventCallback = closeEventCallback;
        }

        public void setClickEventCallback(GUIViewEventCallback<InventoryClickEvent> clickEventCallback) {
            this.clickEventCallback = clickEventCallback;
        }

        public void setDragEventCallback(GUIViewEventCallback<InventoryDragEvent> dragEventCallback) {
            this.dragEventCallback = dragEventCallback;
        }
    }

    public interface GUIClickAction {

        void onClick(InventoryClickEvent clickEvent, GUIView view);

    }

    public Player getPlayer() {
        return player;
    }

    public GUIHolder getGuiHolder() {
        return guiHolder;
    }

    public InventoryView getInventoryView() {
        return inventoryView;
    }

    public GUIViewSettings getSettings() {
        return settings;
    }

    public GUIViewEventCallbackHolder getEventCallbackHolder() {
        return eventCallbackHolder;
    }

    public Map<Integer, GUIClickAction> getActionMap() {
        return actionMap;
    }
}
