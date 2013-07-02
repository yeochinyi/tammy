package org.moomoocow.tammy.gui

import javax.swing.*
import groovy.swing.SwingBuilder
import java.awt.BorderLayout as BL
import groovy.beans.Bindable
import javax.swing.tree.DefaultMutableTreeNode as TreeNode

class MyModel {
   @Bindable int count = 0
}

mboxes = [
	[name: "root@example.com", folders: [[name: "Inbox"], [name: "Trash"]]],
	[name: "test@foo.com", folders: [[name: "Inbox"], [name: "Trash"]]]
]

def model = new MyModel()
new SwingBuilder().edt {
  frame(title: "Java Frame", 
	  size: [100, 100], locationRelativeTo: null, show: true) {
	  menuBar() {
		  menu(text: "File", mnemonic: 'F') {
			  menuItem(text: "Exit", mnemonic: 'X', actionPerformed: {dispose() })
		  }
	  }
	  splitPane {
		  scrollPane(constraints: "left", preferredSize: [160, -1]) {
			  mboxTree = tree(rootVisible: false)
			  //label(text: bind(source: model, sourceProperty: "count", converter: { v ->  v? "Clicked $v times": ''}))
			  //button("Click me!", actionPerformed: { model.count++ })
		  
		  }
		  splitPane(orientation:JSplitPane.VERTICAL_SPLIT, dividerLocation:280) {
			  scrollPane(constraints: "top") { mailTable = table() }
			  scrollPane(constraints: "bottom") { textArea() }
		  }
	  }
	  ["From", "Date", "Subject"].each { mailTable.model.addColumn(it) }
  
	//gridLayout(cols: 1, rows: 2)
  }
	  
	  mboxTree.model.root.removeAllChildren()
	  mboxes.each {mbox ->
		  def node = new TreeNode(mbox.name)
		  mbox.folders.each { folder -> node.add(new TreeNode(folder.name)) }
		  mboxTree.model.root.add(node)
	  }
	  mboxTree.model.reload(mboxTree.model.root)
}

