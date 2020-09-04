
let canvas;
let ctx;

function start() {
    canvas = document.getElementById('myCanvas');
    if (canvas.getContext) {
        ctx = canvas.getContext('2d');
    } else {
        console.error("Canvas not supported!");
    }
    canvas.addEventListener('click', function(evt) {
        returnMousePosition(canvas, evt);
    }, false);
    drawBlankBoard();
    draw();
}

let WIDTH = 80;
let HEIGHT = 80;

let ROWS = 9;
let COLUMNS = 6;

const piece = {};

piece.water = {
    id: -1,
    color: "blue",
    health: 0,
    damage: 0,
    name: ''
};

piece.empty = {
    id: 0,
    color: "#FFFFFF",
    health: 0,
    damage: 0,
    name: '-'
};

piece.bomb = {
    id: 1,
    color: "#828282",
    health: 1,
    damage: 10,
    name: 'B'
};

piece.one = {
    id: 2,
    color: '#E08907',
    health: 1,
    damage: 1,
    name: '1'
};

piece.two = {
    id: 3,
    color: '#642E00',
    health: 2,
    damage: 2,
    name: '2'
};

piece.flag = {
    id: 4,
    color: '#749317',
    health: 1,
    damage: 0,
    name: 'F'
};

let board = [
    [piece.one,piece.one,piece.one,piece.two,piece.one,piece.one],
    [piece.one,piece.one,piece.one,piece.two,piece.one,piece.one],
    [piece.two,piece.one,piece.two,piece.two,piece.one,piece.one],
    [piece.two,piece.one,piece.one,piece.two,piece.one,piece.one],
    [piece.two,piece.one,piece.one,piece.two,piece.one,piece.one],
    [piece.two,piece.one,piece.one,piece.two,piece.one,piece.one],
    [piece.two,piece.one,piece.one,piece.two,piece.one,piece.one],
    [piece.two,piece.bomb,piece.one,piece.bomb,piece.bomb,piece.bomb],
    [piece.two,piece.one,piece.one,piece.bomb,piece.flag,piece.bomb],
];

function draw() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    for(let x = 0; x < board.length; x++) {
        for(let y = 0; y < board[x].length; y++) {
            let boardPiece = getBoardPiece(x, y);
            ctx.fillStyle = boardPiece.color;
            ctx.fillRect(WIDTH * y, HEIGHT * x, WIDTH, HEIGHT);
            ctx.font=(WIDTH * 0.30) + "px Georgia";
            ctx.fillStyle = "#000000";
            ctx.fillText(boardPiece.name, (WIDTH * y) + (WIDTH / 2) - (WIDTH * 0.05), (HEIGHT * x) + (HEIGHT / 2) + (HEIGHT * 0.05), WIDTH)
        }
    }
}

function getBoardPiece(x, y) {
    return board[x][y];
}

function clearBoard() {
    drawBlankBoard();
    draw();
}

function generateSampleBoard() {
    board = [
        [piece.one,piece.one,piece.one,piece.two,piece.one,piece.one],
        [piece.one,piece.one,piece.one,piece.two,piece.one,piece.one],
        [piece.two,piece.one,piece.two,piece.two,piece.one,piece.one],
        [piece.empty,piece.water,piece.empty,piece.empty,piece.water,piece.empty],
        [piece.empty,piece.empty,piece.water,piece.empty,piece.empty,piece.empty],
        [piece.empty,piece.water,piece.empty,piece.empty,piece.water,piece.empty],
        [piece.two,piece.one,piece.one,piece.two,piece.one,piece.one],
        [piece.two,piece.bomb,piece.one,piece.bomb,piece.bomb,piece.bomb],
        [piece.two,piece.one,piece.one,piece.bomb,piece.flag,piece.bomb],
    ];
    draw();
}

function returnMousePosition(inputCanvas, inputEvt) {
    let mousePos = getMousePos(inputCanvas, inputEvt);
    let row = Math.floor(mousePos.y / HEIGHT);
    let column = Math.floor(mousePos.x / WIDTH);
    let message = 'Mouse position: ' + column + ',' + row;
    if (row >= 0 && row < ROWS && column >= 0 && column < COLUMNS) {
        console.log(message);
        switchPiece(row, column);
    } else {
        console.log("Outside of board!");
    }
}

function switchPiece(row, column) {
    if (board[row][column] === piece.empty) {
        board[row][column] = piece.bomb;
    } else if (board[row][column] === piece.bomb) {
        board[row][column] = piece.one;
    } else if (board[row][column] === piece.one) {
        board[row][column] = piece.two;
    } else if (board[row][column] === piece.two) {
        board[row][column] = piece.flag;
    } else if (board[row][column] === piece.flag) {
        board[row][column] = piece.empty;
    }
    draw();
}

function getMousePos(canvas, evt) {
    let rect = canvas.getBoundingClientRect();
    return {
        x: evt.clientX - rect.left,
        y: evt.clientY - rect.top
    };
}

function drawBlankBoard() {
    for(let y = 0; y < board.length; y++) {
        for(let x = 0; x < board[y].length; x++) {
            board[y][x] = piece.empty;
        }
    }
}
