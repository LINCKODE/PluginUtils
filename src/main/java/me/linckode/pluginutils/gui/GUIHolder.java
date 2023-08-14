package me.linckode.pluginutils.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class GUIHolder implements InventoryHolder {

    private final Inventory guiInventory;
    private GUIView guiView;
    private final Plugin plugin;
    private final GUIView.GUIViewEventCallbackHolder eventCallbackHolder = new GUIView.GUIViewEventCallbackHolder();
    private final Map<Integer, GUIView.GUIClickAction> actionMap = new HashMap<>();


    /* Constructor */
    public GUIHolder(int rows, String title, Plugin plugin) {
        guiInventory = Bukkit.createInventory(this, rows * 9, title);
        this.plugin = plugin;
    }

    /* Methods */
    public GUIView openInventory(Player player, GUIView.GUIViewSettings viewSettings) {
        guiView = new GUIView(player, this, player.openInventory(this.getInventory()), actionMap, viewSettings, eventCallbackHolder);
        return guiView;
    }

    public static void closeInventory(Player player) {
        player.closeInventory();
    }

    public void addAction(int index, GUIView.GUIClickAction action){
        actionMap.put(index, action);
    }

    public void addActionToIndices(GUIView.GUIClickAction action, int... indices){
        for (int index : indices){
            addAction(index, action);
        }
    }

    public void addItemAndAction(int index, ItemStack itemStack, GUIView.GUIClickAction action){
        getInventory().setItem(index, itemStack);
        addAction(index, action);
    }

    public void ItemAndActionToIndices(ItemStack itemStack, GUIView.GUIClickAction action, int... indices){
        for (int index : indices){
            addItemAndAction(index, itemStack, action);
        }
    }

    /* Getters and Setters */
    @Override
    public Inventory getInventory() {
        return guiInventory;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public GUIView getGuiView() {
        return guiView;
    }

    public GUIView.GUIViewEventCallbackHolder getEventCallbackHolder() {
        return eventCallbackHolder;
    }

    public Map<Integer, GUIView.GUIClickAction> getActionMap() {
        return actionMap;
    }
}
