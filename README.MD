# LINCKODE's PluginUtils

This library is intended to help bukkit plugin developers with tedious stuff during plugin development.

## `!!! In early development !!!`

> Currently only `1.20.X` is supported, but will try to get working on older versions as well.

## Current features:

- [x] GUIs

## Planned Features:



- Scoreboards
- Easy JSON POJO serialization / deserialization
- Command system
- Chat text input
- Database connection
- HTTP requests
- More?

## GUI example
```java
GUIHolder holder = new GUIHolder(3, "Test", plugin);

        holder.addItemAndAction(0, new ItemStack(Material.GRASS_BLOCK) , (clickEvent, view) -> {
            switch (clickEvent.getClick()) {
                case LEFT -> view.topInventory().setItem(0, new ItemStack(Material.DIRT));
                case RIGHT -> view.topInventory().setItem(0, new ItemStack(Material.GRASS_BLOCK));
            }
        });
        holder.addItemAndAction(1, new ItemStack(Material.SAND) , (clickEvent, view) -> {
            switch (clickEvent.getClick()) {
                case LEFT -> view.setTitle("LEFT");
                case RIGHT -> view.setTitle("RIGHT");
            }
        });
        holder.addItemAndAction(2, new ItemStack(Material.REDSTONE_BLOCK) , (clickEvent, view) -> {
            switch (clickEvent.getClick()) {
                case LEFT -> {
                    view.setTitle("Left Shift Click " + ChatColor.GOLD + "enabled");
                    view.getSettings().getAllowedClickTypes().add(ClickType.SHIFT_LEFT);
                    view.topInventory().getItem(2).setType(Material.DIAMOND_BLOCK);
                }
                case RIGHT -> {
                    view.setTitle("Left Shift Click " + ChatColor.RED + "disabled");
                    view.getSettings().getAllowedClickTypes().remove(ClickType.SHIFT_LEFT);
                    view.topInventory().getItem(2).setType(Material.REDSTONE_BLOCK);
                }
            }
        });
        holder.getEventCallbackHolder().setClickEventCallback((clickEvent, view) -> {
            if (clickEvent.getClick() != ClickType.LEFT) return;
            view.setTitle(ChatColor.GOLD + "Clicked generic slot #" + clickEvent.getRawSlot());
            view.setItemAtRawIndex(clickEvent.getRawSlot(), new ItemStack(Material.GOLDEN_APPLE));

        });
        GUIView view = holder.openInventory(event.getPlayer(), GUIView.GUIViewSettings.defaults());
```
