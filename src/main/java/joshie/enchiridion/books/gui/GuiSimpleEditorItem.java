package joshie.enchiridion.books.gui;

import java.util.ArrayList;

import joshie.enchiridion.EConfig;
import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IBookEditorOverlay;
import joshie.lib.editables.IItemSelectable;
import joshie.lib.helpers.ItemListHelper;
import net.minecraft.item.ItemStack;

public class GuiSimpleEditorItem extends AbstractGuiOverlay {
	public static final GuiSimpleEditorItem INSTANCE = new GuiSimpleEditorItem();
	public IItemSelectable selectable = null;
	private ArrayList<ItemStack> sorted;
    private int position;
    
	protected GuiSimpleEditorItem() {}

	public IBookEditorOverlay setItem(IItemSelectable item) {
		selectable = item;
		return this;
	}
    
    @Override
    public void draw(int mouseX, int mouseY) {
    	if (sorted != null) {
            int j = 0;
            int k = 0;
            for (int i = position; i < position + 138; i++) {
                if (i >= 0 && i < sorted.size()) {
                    //1F > 0.75F, 4 > 5, 16 > 13 + Econfig.editorXPos + 4, -30 > + Econfig. toolBarYPos + 12
                    EnchiridionAPI.draw.drawStack(sorted.get(i), (j * 12) + EConfig.editorXPos + 7, (k * 12) + EConfig.toolbarYPos + 14, 0.75F);

                    j++;

                    if (j > 5) {
                        j = 0;
                        k++;
                    }
                }
            }
        } else updateSearch(GuiSimpleEditor.INSTANCE.getTextField());
    }

	@Override
	public boolean mouseClicked(int mouseX, int mouseY) {
		if (sorted != null) {
			int j = 0;
	        int k = 0;
			for (int i = position; i < position + 138; i++) {
	            if (i >= 0 && i < sorted.size()) {
	            	if (mouseX >= (j * 12) + EConfig.editorXPos + 7 && mouseX <= (j * 12) + EConfig.editorXPos + 19) {
	            		if (mouseY >= (k * 12) + EConfig.toolbarYPos + 14 && mouseY <= (k * 12) + EConfig.toolbarYPos + 26) {
	            			System.out.println("SETTING");
	            			if (selectable != null) { 
		                		selectable.setItemStack(sorted.get(i));
		                		
		                		return true;
		                	}
	            		}
	            	}
	
	                j++;
	
	                if (j > 5) {
	                    j = 0;
	                    k++;
	                }
	            }
	        }
		}
		
		return false;
	}
	
	@Override
	public void scroll(boolean down, int mouseX, int mouseY) {
		if (mouseX >= EConfig.editorXPos + 7 && mouseX <= EConfig.editorXPos + 91) {
    		if (mouseY >= EConfig.toolbarYPos + 14 && mouseY <= EConfig.toolbarYPos + 302) {
    			if (down) {
                    position = Math.min(sorted.size() - 200, position + 6);
                } else {
                    position = Math.max(0, position - 6);
                }
    		}
		}
	}
	
	@Override
	public void updateSearch(String search) {
        ItemListHelper.addInventory();

        if (search == null || search.equals("")) {
            sorted = new ArrayList(ItemListHelper.allItems());
        } else {
            position = 0;
            sorted = new ArrayList();
            for (ItemStack stack : ItemListHelper.allItems()) {
                try {
                    if (stack != null && stack.getItem() != null) {
                        if (stack.getDisplayName().toLowerCase().contains(search.toLowerCase())) {
                            sorted.add(stack);
                        }
                    }
                } catch (Exception e) {}
            }
        }
    }
}
