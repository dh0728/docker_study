const express = require('express')
const app = express();


app.listen(8000, ()=> {
    
    console.log('서버 실행 중 http://localhost:8000');
});

app.get('/', (req, res) => {
    res.send('안녕')
})