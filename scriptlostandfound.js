
function screens1(){
  window.location.href = "lostandfound2.html";
}

function screens2() {
  window.location.href = "lostandfound3.html";
}

function screens0() {
  window.location.href = "lostandfound1.html";
}

function screensAbout() {
  window.location.href = "about.html";
}
document.addEventListener('DOMContentLoaded', function() {
  const finalBtn = document.getElementById('final1');
  const MAX_BYTES = 1024 * 1024 * 4;
  window._lf_image = window._lf_image || null;
  
  function getItems() {
    const raw = localStorage.getItem('lf_items');
    if (!raw) return [];
    try { return JSON.parse(raw); } catch (e) { return []; }
  }

  function populateFilters(items) {
    if (!items || !items.length) return;
    const catEl = document.getElementById('filterCategory');
    const locEl = document.getElementById('filterLocation');
    if (!catEl && !locEl) return;

    function uniques(arr) {
      return Array.from(new Set(arr.map(x => x || '').filter(x => x !== '')));
    }

    if (catEl) {
      const current = catEl.value || '';
      const cats = uniques(items.map(it => it.catego));
      catEl.innerHTML = '';
      const optAll = document.createElement('option'); optAll.value = ''; optAll.textContent = 'All categories'; catEl.appendChild(optAll);
      cats.forEach(function(c) { const o = document.createElement('option'); o.value = c; o.textContent = c; catEl.appendChild(o); });
      if (current) catEl.value = current;
    }

    if (locEl) {
      const currentL = locEl.value || '';
      const locs = uniques(items.map(it => it.locat));
      locEl.innerHTML = '';
      const optAllL = document.createElement('option'); optAllL.value = ''; optAllL.textContent = 'All locations'; locEl.appendChild(optAllL);
      locs.forEach(function(l) { const o = document.createElement('option'); o.value = l; o.textContent = l; locEl.appendChild(o); });
      if (currentL) locEl.value = currentL;
    }
  }

  function saveItems(items) {
    const s = JSON.stringify(items);
    if (s.length <= MAX_BYTES) {
      localStorage.setItem('lf_items', s);
      return true;
    }
    return false;
  }

  function deleteItemById(id) {
    const items = getItems().filter(it => String(it.id) !== String(id));
    saveItems(items);
    renderItems();
  }

  function clearAll() {
    localStorage.removeItem('lf_items');
    renderItems();
  }

  function clearFormInputs() {
    const namesInput = document.getElementById('text1'); if (namesInput) namesInput.value = '';
    const dateInput = document.getElementById('text2'); if (dateInput) dateInput.value = '';
    const descInput = document.getElementById('text3'); if (descInput) descInput.value = '';
    const categoInput = document.getElementById('text4'); if (categoInput) categoInput.value = '';
    const locatInput = document.getElementById('text5'); if (locatInput) locatInput.value = '';
    const fileInput = document.querySelector('input[type=file]'); if (fileInput) fileInput.value = '';
    const imageOut = document.getElementById('image'); if (imageOut) imageOut.src = '';
  }

  function renderItems() {
    const output = document.getElementById('output');
    if (!output) return;
    output.innerHTML = '';

    const items = getItems();
    if (!items.length) return;
    populateFilters(items);

    const filterCategoryEl = document.getElementById('filterCategory');
    const filterLocationEl = document.getElementById('filterLocation');
    const filterCategory = filterCategoryEl ? filterCategoryEl.value : '';
    const filterLocation = filterLocationEl ? filterLocationEl.value : '';

    const filtered = items.filter(function(item) {
      if (!item) return false;
      if (filterCategory && String(item.catego || '') !== String(filterCategory)) return false;
      if (filterLocation && String(item.locat || '') !== String(filterLocation)) return false;
      return true;
    });

    const visibleItems = filtered;

    const clearBtn = document.createElement('button');
    clearBtn.type = 'button';
    clearBtn.textContent = 'Clear All';
    clearBtn.addEventListener('click', function() { if (confirm('Clear all saved items?')) clearAll(); });
    output.appendChild(clearBtn);

    if (!visibleItems.length) {
      const p = document.createElement('p');
      p.textContent = 'No items match your filter.';
      output.appendChild(p);
    }

    visibleItems.forEach(item => {
      if (!item) return;
      const wrap = document.createElement('div');
      wrap.className = 'lf-item';

      if (item.image) {
        const img = document.createElement('img');
        img.src = item.image;
        img.alt = item.name || '';
        wrap.appendChild(img);
      }

      ['name','date','desc','catego','locat'].forEach(function(key){
        const p = document.createElement('p'); p.className = 'outputcode';
        const label = key === 'name' ? 'The Items name: ' : key === 'date' ? 'Item was found on ' : key === 'desc' ? 'Item info: ' : key === 'catego' ? 'The type of item is: ' : 'Item was found in: ';
        p.textContent = label + (item[key] || '');
        wrap.appendChild(p);
      });

      const del = document.createElement('button'); del.type = 'button'; del.textContent = 'Delete';
      del.addEventListener('click', function(){ if (confirm('Delete this item?')) deleteItemById(item.id); });
      wrap.appendChild(del);

      output.appendChild(wrap);
      const hr = document.createElement('hr'); output.appendChild(hr);
    });
  }

  if (finalBtn) {
    const form = document.getElementById('lostItemForm');
    function handleSubmit(e) {
      if (e && e.preventDefault) e.preventDefault();
      const names = document.getElementById('text1')?.value || '';
      const date = document.getElementById('text2')?.value || '';
      const desc = document.getElementById('text3')?.value || '';
      const catego = document.getElementById('text4')?.value || '';
      const locat = document.getElementById('text5')?.value || '';

      const item = {
        id: Date.now(),
        name: names,
        date: date,
        desc: desc,
        catego: catego,
        locat: locat,
        image: window._lf_image || ''
      };

      const items = getItems();
      items.push(item);

      if (!saveItems(items)) {
        alert('Cannot save: too much data (try removing items or use smaller images).');
        return;
      }

      renderItems();
      clearFormInputs();
    }

    if (form) form.addEventListener('submit', handleSubmit);
    finalBtn.addEventListener('click', handleSubmit);
  }

  const applyBtn = document.getElementById('applyFilterBtn');
  const clearFilterBtn = document.getElementById('clearFilterBtn');
  if (applyBtn) applyBtn.addEventListener('click', function(){ renderItems(); });
  if (clearFilterBtn) clearFilterBtn.addEventListener('click', function(){
    const catEl = document.getElementById('filterCategory'); if (catEl) catEl.value = '';
    const locEl = document.getElementById('filterLocation'); if (locEl) locEl.value = '';
    renderItems();
  });

  renderItems();
});

var loadFile = function(event) {
  var file = event.target.files && event.target.files[0];
  if (!file) return;
  var imageout = document.getElementById('image');
  function compress(file, maxSize, quality, cb) {
    const url = URL.createObjectURL(file);
    const img = new Image();
    img.onload = function() {
      let w = img.naturalWidth;
      let h = img.naturalHeight;
      if (w > maxSize || h > maxSize) {
        const ratio = w / h;
        if (w > h) { w = maxSize; h = Math.round(maxSize / ratio); }
        else { h = maxSize; w = Math.round(maxSize * ratio); }
      }
      const canvas = document.createElement('canvas');
      canvas.width = w;
      canvas.height = h;
      const ctx = canvas.getContext('2d');
      ctx.drawImage(img, 0, 0, w, h);
      const dataUrl = canvas.toDataURL('image/jpeg', quality);
      URL.revokeObjectURL(url);
      cb(dataUrl);
    };
    img.onerror = function() { URL.revokeObjectURL(url); cb(null); };
    img.src = url;
  }

  compress(file, 400, 0.5, function(dataUrl) {
    if (!dataUrl) return;
    window._lf_image = dataUrl;
    if (imageout) imageout.src = dataUrl;
  });
};

