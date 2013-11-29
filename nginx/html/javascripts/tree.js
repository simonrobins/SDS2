function hide(id)
{
  _hideshow(document.getElementById(id), 'none');
}
function show(id)
{
  _hideshow(document.getElementById(id), '');
}
function _hideshow(tree, display)
{
  for(var child = tree.firstChild; child; child = child.nextSibling)
  {
    if(child.tagName == 'DIV' && child.className == 'child')
    {
      child.style.display = display;
      _hideshow(child, display);
    }
  }
}
function hideshow(self)
{
	var children = self.nextSibling;
	while(children.tagName != 'DIV')
		children = children.nextSibling;
	children.style.display = (children.style.display == 'none') ? '' : 'none';
}
