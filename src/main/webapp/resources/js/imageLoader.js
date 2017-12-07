function el(id) {
    return document.getElementById(id);
} // Get elem by ID

var ctx = el("canvas").getContext("2d");
var baseX = 0;
var baseY = 0;
var lastPointX = 0;
var lastPointY = 0;
var scale = 1;
var lastScale = 1;
var maxX = 0;
var maxY = 0;
var cutoutWidth = 40;
var image = new Image();
var click = false;
var windowWidth = 170;
var vData = null;

function init() {
    image.src = "../../resources/img/defaultAvatar.png";
    image.addEventListener("load", function () {
        drawImage(0, 0);
    });
}

function readImage() {
    if (this.files && this.files[0]) {
        var FR = new FileReader();
        FR.onload = function (e) {
            var img = new Image();
            img.addEventListener("load", function () {
                ctx.drawImage(img, 0, 0);
            });
            img.src = e.target.result;
            image.src = e.target.result;
            scale = 1;
            lastScale = 1;
            el("scaleSlider").value = 1;
        };
        FR.readAsDataURL(this.files[0]);
    }
    el("canvas").style.display = "inline";
    minScale();
}

function minScale() {
    var minScaleX = (210 - baseX) / maxX;
    var minScaleY = (210 - baseY) / maxY;
}
function drawImage(x, y) {
     var width = ctx.canvas.width;
    var height = ctx.canvas.height;
    ctx.clearRect(0, 0, width, height);
    baseX = baseX + (x - lastPointX);
    baseY = baseY + (y - lastPointY);
    maxY = image.height;
    maxX = image.width;
    if (baseX > 40) {
        baseX = 40;
    }
    if (baseY > 40) {
        baseY = 40;
    }
    if (baseX < (210 - maxX * scale)) {
        baseX = 210 - maxX * scale;
    }
    if (baseY < (210 - maxY * scale)) {
        baseY = 210 - maxY * scale;
    }
    lastPointX = x;
    lastPointY = y;
    ctx.drawImage(image, baseX, baseY, Math.floor(image.width * scale), Math.floor(image.height * scale));
    drawCutout();
    showCropedImage();
}

function drawCutout() {
    var canvas = el("canvas");
    ctx.fillStyle = "rgba(0,0,0,0.65)";
    ctx.beginPath();
    ctx.rect(0, 0, ctx.canvas.width, ctx.canvas.height);
    ctx.moveTo(cutoutWidth, cutoutWidth);
    ctx.lineTo(cutoutWidth, windowWidth + cutoutWidth);
    ctx.lineTo(cutoutWidth + windowWidth, cutoutWidth + windowWidth);
    ctx.lineTo(cutoutWidth + windowWidth, cutoutWidth);
    ctx.closePath();
    ctx.fill();
    ctx.lineWidth = 2;
    ctx.strokeStyle = "#ffffff";
    ctx.stroke();
}
function onMouseDown(e) {
    e.preventDefault();
    var loc = windowToCanvas(e.clientX, e.clientY);
    click = true;
    lastPointX = loc.x;
    lastPointY = loc.y;
}

function onMouseMove(e) {
    e.preventDefault();
    if (click) {
        var loc = windowToCanvas(e.clientX, e.clientY);
        drawImage(loc.x, loc.y);

    }
}

function onMouseUp(e) {
    e.preventDefault();
    click = false;
}

function windowToCanvas(x, y) {
    var canvas = ctx.canvas;
    var bbox = canvas.getBoundingClientRect();
    return {
        x: x - bbox.left * (canvas.width / bbox.width),
        y: y - bbox.top * (canvas.height / bbox.height)
    };
}

function showCropedImage() {
    var temp_context, temp_canvas;
    temp_canvas = document.createElement("canvas");
    temp_context = temp_canvas.getContext("2d");
    temp_canvas.width = windowWidth;
    temp_canvas.height = windowWidth;
    temp_context.drawImage(ctx.canvas, cutoutWidth, cutoutWidth, windowWidth, windowWidth, 0, 0, windowWidth, windowWidth);
    vData = temp_canvas.toDataURL();
    el("canvasResult").src = vData;
    el("canvasResult").style.display = "inline";

}

function sendData() {
    document.getElementById('sendFile').value = vData;
    document.forms["form1"].submit();
}
function updateScale(e) {

    scale = e.target.value;
    if (baseX < (210 - maxX * scale) || baseY < (210 - maxY * scale)) {
        if (baseX > 40 || baseY > 40) {
            scale = lastScale;
            e.target.value = lastScale;
        } else {
            baseX = baseX + 1;
            baseY = baseY + 1;
            lastScale = scale;
        }
    } else {
        lastScale = scale;
    }
    drawImage(lastPointX, lastPointY);
}

el("canvas").addEventListener("mousedown", onMouseDown, false);
el("canvas").addEventListener("mousemove", onMouseMove, false);
el("canvas").addEventListener("mouseup", onMouseUp, false);
el("scaleSlider").addEventListener("input", updateScale, false);
el("fileUpload").addEventListener("change", readImage, false);
init();