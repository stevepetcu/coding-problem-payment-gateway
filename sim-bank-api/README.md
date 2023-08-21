# Sim Bank API

## 1. Running the tests/application on your local machine

### 1.1 Requirements

- node >= 18.15.0
- pnpm
- (Optionally) nvm

### 1.2 Running the tests locally

TODO: Add tests.

### 1.3 Running the API locally

To run the Sim Bank API locally:
- Open a terminal window
- Navigate to the `sim-bank-api` directory
- Execute the following commands (if only one version of node is present on the system, skip executing `nvm use`):
```commandline
nvm use
pnpm install
npm run dev
```

The application will start by default on port `5050`. If that port is unavailable, add a `.env` file
to the `sim-bank-api` folder and define the port:
```dotenv
PORT=<PORT_NUMBER>
```

## 2. Areas for improvement

- Test automation
- Improved error responses
- Implement authorisation
- Application architecture improvements etc.
