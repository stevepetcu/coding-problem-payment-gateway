import 'dotenv/config';
import ExpressConfig from './express/express.config.js';

const app = ExpressConfig();

console.log('Process env: ', process.env.PORT);

const PORT = process.env.PORT || 5050;

app.listen(PORT, () => console.log(`Server started on port ${PORT}.`));
