# WorkSaathi Frontend 🛠️✨

Modern, responsive web application for **WorkSaathi** — India's trusted on-demand local services platform connecting customers with verified skilled professionals (Electricians, Plumbers, Carpenters, AC Technicians, Home Cleaners, and more).

---

## 🚀 Features

- **Multi-Role Portals**: Seamless unified interface supporting Customer, Worker, and Administrator views.
- **Worker Search & Discovery**: Real-time filtering by service category, price, rating, proximity, and availability.
- **End-to-End Booking Workflow**: Multi-step booking modal with interactive date/slot selection and instant quotes.
- **Worker Management Dashboard**: Real-time job requests, status transitions (`REQUESTED` ➔ `ACCEPTED` ➔ `ON_THE_WAY` ➔ `ARRIVED` ➔ `IN_PROGRESS` ➔ `COMPLETED`).
- **Admin Verification Control Panel**: Review unverified worker registrations and approve credentials with 1-click.
- **Authentication**: JWT-backed authentication with 1-click demo role switchers.
- **Automated Vitest Suite**: 100% passing component and API client test coverage with `@testing-library/react`.

---

## 🛠️ Tech Stack

- **Framework**: React 18 + Vite
- **Styling**: Vanilla CSS (Custom Design System with responsive variables)
- **API Client**: Fetch with automatic Bearer token injection
- **Testing**: Vitest + Testing Library + jsdom
- **Icons & Visuals**: Pure CSS & SVG iconography

---

## 🔧 Getting Started

### 1. Install Dependencies
```bash
npm install
```

### 2. Configure Environment (Optional)
Create `.env` or use defaults:
```env
VITE_API_URL=http://localhost:8080/api/v1
```

### 3. Start Development Server
```bash
npm run dev
```
App will be available at: [http://localhost:5173](http://localhost:5173)

### 4. Run Automated Tests
```bash
npm run test
```

---

## 📁 Project Structure

```
worksaathi-frontend/
├── src/
│   ├── __tests__/           # Vitest unit & component test suites
│   │   ├── api.test.js      # REST API client tests
│   │   └── App.test.jsx     # UI component integration tests
│   ├── services/
│   │   └── api.js           # Centralized backend REST API client
│   ├── main.jsx             # React component hierarchy & entrypoint
│   ├── setupTests.js        # Jest-DOM matchers setup
│   └── styles.css           # Design tokens & responsive styles
├── index.html
├── vite.config.js           # Vite & Vitest configuration
├── package.json
└── README.md
```

---

## 🧪 Testing Results

- **Vitest Suites**: 2 Passed (2/2)
- **Assertions**: 13 Passed (100% Success)

---

## 📄 License
MIT © WorkSaathi
