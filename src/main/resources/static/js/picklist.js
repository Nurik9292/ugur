const front = document.getElementById('front');
const back = document.getElementById('back');

const sourceFrontWrapper = front ? front.querySelector('.p-picklist-source-wrapper') : null;
const targetFrontWrapper = front ? front.querySelector('.p-picklist-target-wrapper') : null;

const sourceBackWrapper = back ? back.querySelector('.p-picklist-source-wrapper') : null;
const targetBackWrapper = back ? back.querySelector('.p-picklist-target-wrapper') : null;

const sourceFrontControls = front ? front.querySelector('.p-picklist-source-controls') : null;
const targetFrontControls = front ? front.querySelector('.p-picklist-target-controls') : null;

const sourceBackControls = back ? back.querySelector('.p-picklist-source-controls') : null;
const targetBackControls = back ? back.querySelector('.p-picklist-target-controls') : null;

const groupFrontButtonsRight = sourceFrontControls ? sourceFrontControls.querySelectorAll('.p-button') : [];
const groupFrontButtonsLeft = targetFrontControls ? targetFrontControls.querySelectorAll('.p-button') : [];

const groupBackButtonsRight = sourceBackControls ? sourceBackControls.querySelectorAll('.p-button') : [];
const groupBackButtonsLeft = targetBackControls ? targetBackControls.querySelectorAll('.p-button') : [];

const buttonFrontRight = document.getElementById('front-right');
const buttonFrontLeft = document.getElementById('front-left');

const buttonBackRight = document.getElementById('back-right');
const buttonBackLeft = document.getElementById('back-left');

const buttonFrontMoveAllRight = document.getElementById('front-right-all');
const buttonFrontMoveAllLeft = document.getElementById('front-left-all');

const buttonBackMoveAllRight = document.getElementById('back-right-all');
const buttonBackMoveAllLeft = document.getElementById('back-left-all');


function handleItemClick(event) {
   
    const item = event.target.closest('li.p-picklist-item'); 
    const sourceFrontListWrapper = document.getElementById('front_id_source_list');
    const sourceBackListWrapper = document.getElementById('back_id_source_list');

    if (!item) {
        return; 
    }

    const sourceList = item.parentNode;

    let button = [];
    let dropButton = [];

    if(sourceList.id === 'front_id_source_list' || sourceList.id === 'front_id_target_list') {
        button = sourceList === sourceFrontListWrapper ? buttonFrontRight : buttonFrontLeft;
        dropButton = sourceList === sourceFrontListWrapper ? groupFrontButtonsRight : groupFrontButtonsLeft;
    }else {
        button = sourceList === sourceBackListWrapper ? buttonBackRight : buttonBackLeft;
        dropButton = sourceList === sourceBackListWrapper ? groupBackButtonsRight : groupBackButtonsLeft;
    }

    const hasFocus = item.classList.contains('p-focus');     
    toggleFocus(item, !hasFocus);


        if(hasFocus) {
            const hasRemainingFocus = Array.from(getItemList(item.parentNode)).some(i => i.classList.contains('p-focus'));
            updateButtonState(button, !hasRemainingFocus);
            updateButtonStates(dropButton, !hasRemainingFocus);                 
        }else {
            updateButtonState(button, false);
            updateButtonStates(dropButton, false);
        }
}

function updateSourceAndTarget() {
    sourceFront = getItemList(sourceFrontWrapper) || [];
    targetFront = getItemList(targetFrontWrapper) || [];
    sourceBack = getItemList(sourceBackWrapper) || [];
    targetBack= getItemList(targetBackWrapper) || [];
  }

function getItemList(wrapper) {
        return wrapper.querySelectorAll('li.p-picklist-item');
}

function updateButtonStates(buttons, isDisabled) {
    buttons.forEach(button => {
        updateButtonState(button, isDisabled);
    });
}


function updateButtonState(button, isDisabled) {
    button.classList.toggle('p-disabled', isDisabled);
    isDisabled ? button.setAttribute('disabled', 'disabled') : button.removeAttribute('disabled');
  }


function moveItem(item, fromList, toList) {
    fromList.removeChild(item);
    toList.appendChild(item);
} 

function attachItemEvent(itemList) { 

    itemList.forEach(item => {
        item.addEventListener('click', handleItemClick);
   });
}

function moveAllFront(side) {
    const sourceList = document.getElementById('front_id_source_list');
    const targetList = document.getElementById('front_id_target_list');
    const items = Array.from(side === 'source' ?
        sourceList.querySelectorAll('.p-picklist-item') : targetList.querySelectorAll('.p-picklist-item'));

    moveAll(items, sourceList, targetList, side, 'front');
}

function moveAllBack(side) {
    const sourceList = document.getElementById('back_id_source_list');
    const targetList = document.getElementById('back_id_target_list');
    const items = Array.from(side === 'source' ?
        sourceList.querySelectorAll('.p-picklist-item') : targetList.querySelectorAll('.p-picklist-item'));

    moveAll(items, sourceList, targetList, side, 'back');
}

function moveAll(items, sourceList, targetList, side, line) {

    items.forEach(item => {
       toggleFocus(item, false);
        if(side === 'source')
            moveItem(item, sourceList, targetList);
        else
            moveItem(item, targetList, sourceList);

    });

    updateSourceAndTarget();

    let sourceFocusItems = Array.from(sourceList.querySelectorAll('.p-focus'));
    let targetFocusItems = Array.from(targetList.querySelectorAll('.p-focus'));

    const buttonLeft = line === 'front' ? buttonFrontLeft : buttonBackLeft;
    const buttonRight = line === 'front' ? buttonFrontRight : buttonBackRight;
    const groupButtonLeft = line === 'front' ? groupFrontButtonsLeft : groupBackButtonsLeft;
    const groupButtonRight = line === 'front' ? groupFrontButtonsRight : groupBackButtonsRight;
    const buttonMoveAllLeft = line === 'front' ? buttonFrontMoveAllLeft : buttonBackMoveAllLeft;
    const buttonMoveAllRight = line === 'front' ? buttonFrontMoveAllRight : buttonBackMoveAllRight;
    const source = line === 'front' ? sourceFront : sourceBack;
    const target = line === 'front' ? targetFront : targetBack;

    detachItemEvent(target);
    detachItemEvent(source);

    attachItemEvent(source, buttonLeft, groupButtonLeft);
    attachItemEvent(target, buttonRight, groupButtonRight);

    updateButtonStates(groupButtonRight, sourceFocusItems.length === 0);
    updateButtonStates(groupButtonLeft, targetFocusItems.length === 0);
    updateButtonState(buttonMoveAllRight, source.length === 0);
    updateButtonState(buttonMoveAllLeft, target.length === 0);
    updateButtonState(buttonRight, Array.from(sourceList.querySelectorAll('.p-focus')).length === 0);
    updateButtonState(buttonLeft, Array.from(targetList.querySelectorAll('.p-focus')).length === 0);

    
}

function moveSelectedFront(side) {
    let sourceList = document.getElementById('front_id_source_list');
    let targetList = document.getElementById('front_id_target_list');

    moveSelected(getItems(side, sourceList, targetList), sourceList, targetList, side, 'front');
}

function moveSelectedBack(side) {
    let sourceList = document.getElementById('back_id_source_list');
    let targetList = document.getElementById('back_id_target_list');

    moveSelected(getItems(side, sourceList, targetList), sourceList, targetList, side, 'back');
}

function getItems(side, sourceList, targetList) {
    return Array.from(side === 'source' ? sourceList.querySelectorAll('.p-focus') :
        targetList.querySelectorAll('.p-focus'));
}


function moveSelected(items, sourceList, targetList, side, line) {

    items.forEach(item => {
        toggleFocus(item, false);

        if(side === 'source')
            moveItem(item, sourceList, targetList);
        else
            moveItem(item, targetList, sourceList);
    
    });
   

    let sourceFocusItems = Array.from(sourceList.querySelectorAll('.p-focus'));
    let targetFocusItems = Array.from(targetList.querySelectorAll('.p-focus'));

    updateSourceAndTarget();

    const source = line === 'front' ? sourceFront : sourceBack;
    const target = line === 'front' ? targetFront : targetBack;
    const buttonLeft = line === 'front' ? buttonFrontLeft : buttonBackLeft;
    const buttonRight = line === 'front' ? buttonFrontRight : buttonBackRight;
    const groupButtonLeft = line === 'front' ? groupFrontButtonsLeft : groupBackButtonsLeft;
    const groupButtonRight = line === 'front' ? groupFrontButtonsRight : groupBackButtonsRight;

    detachItemEvent(target);
    detachItemEvent(source);
    attachItemEvent(target)
    attachItemEvent(source)
    
    
    updateButtonStates(groupButtonRight, sourceFocusItems.length === 0);
    updateButtonStates(groupButtonLeft, targetFocusItems.length === 0);
    updateButtonState(buttonRight, sourceFocusItems.length === 0);
    updateButtonState(buttonLeft, targetFocusItems.length === 0);
  
}

function toggleFocus(item, isDisabled) {
    isDisabled ?  item.classList.add('p-focus') : item.classList.remove('p-focus');
    isDisabled ?  item.classList.add('p-highlight') : item.classList.remove('p-highlight');
    item.setAttribute('data-p-focused', isDisabled);
    item.setAttribute('aria-selected', isDisabled);
    item.setAttribute('data-p-highlight', isDisabled);
  }

function detachItemEvent(itemList) {
    itemList.forEach(item => {
        item.removeEventListener('click', handleItemClick);
    });
}

let sourceFront = [];
let targetFront = [];
let sourceBack = [];
let targetBack = [];

if(sourceFrontWrapper) {
    sourceFront = getItemList(sourceFrontWrapper) || [];
    targetFront = getItemList(targetFrontWrapper) || [];
    sourceBack = getItemList(sourceBackWrapper) || [];
    targetBakc = getItemList(targetBackWrapper) || [];

    updateButtonState(buttonFrontMoveAllRight, sourceFront.length === 0);
    updateButtonState(buttonFrontMoveAllLeft, targetFront.length === 0);
    updateButtonState(buttonBackMoveAllRight, sourceBack.length === 0);
    updateButtonState(buttonBackMoveAllLeft, targetBakc.length === 0);

    attachItemEvent(sourceFront);
    attachItemEvent(targetFront);
    attachItemEvent(sourceBack);
    attachItemEvent(targetBakc);
}

/////////////////////// snap ////////////////

function moveUp(side) {
    const list = side == 'source' ? document.getElementById('front_id_source_list') : document.getElementById('front_id_target_list');
    const selectedItem = list.querySelector('.p-focus'); 

    if (!selectedItem) return; 

    const previousItem = selectedItem.previousElementSibling; 
    if (!previousItem) return; 
  
    list.insertBefore(selectedItem, previousItem); 
  }
  
  function moveDown(side) {
    const list = side == 'source' ? wrapper.getElementById('front_id_source_list') : wrapper.getElementById('front_id_target_list');
    const selectedItem = list.querySelector('.p-focus'); 

    if (!selectedItem) return; 
  
    const nextItem = selectedItem.nextElementSibling;
    if (!nextItem) return; 

    list.insertBefore(nextItem, selectedItem); 
     
  }

  function moveToTop(side) {
    const list = side == 'source' ? document.getElementById('front_id_source_list') : document.getElementById('front_id_target_list');
    const selectedItem = list.querySelector('.p-focus'); 

    list.insertBefore(selectedItem, list.firstChild);
  }

  function moveToBottom(side) {
    const list = side == 'source' ? document.getElementById('front_id_source_list') : document.getElementById('front_id_target_list');
    const selectedItem = list.querySelector('.p-focus'); 

    list.insertBefore(selectedItem, list.lastChild);
  }