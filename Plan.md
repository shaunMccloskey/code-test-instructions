# URL Shortener Coding Task Plan

## High Level Architecture

Split into two parts within the repo:
1. Frontend application built using React and Material UI
2. Backend Spring application with controller, service, model, repository

Due to the data structure of a shortened URL object containing only two fields and the nature of the application and how it handles scale, decided to use NoSQL. For simplicity, will use MongoDB.

For each of the frontend and backend, can Docker the applications and use Docker Compose to deploy them both with a simple MongoDB image.

### Folder Structure
```
/url-shortener-backend
  | - Spring application (dockerized)
/url-shortener-frontend
  | - React application (dockerized)
docker-compose.yml - builds MongoDB, backend, and frontend; connects network and defines env variables
```

## Implementation Backend Details

### Model
- `UrlPair` - stores the alias and URL

### Repository
- Pull object by alias
- Check pre-existing alias (must be unique)
- Delete by alias

### Service
- `shortenUrl()` - takes in alias and URL; if no alias given, generate random alias; check if alias is unique and save
- `generateAlias()` - generate random string
- `getUrlPairByAlias()` - helper to get URL
- `deleteUrlPair()` - helper to delete URL
- `getAllUrls()` - return all URL pairs

### Controller
- `POST /shorten` - call service to save URL pair
- `GET /{alias}` - redirect to original URL
- `DELETE /{alias}` - delete URL pair
- `GET /urls` - return all URL pairs

### Code Quality and Linters
- Use Spotless with Google standards
- Added to POM to run check and apply formatting
- Added as pre-commit hook - any commits will auto-run check and fail if not matching standards


## Implementation Frontend Details

### Technology Stack
- React 18+ with TypeScript
- Material UI (MUI) for component library
- Axios for API calls

### Project Structure
```
/src
  /components
    - UrlShortenerForm.tsx - form to input URL and optional alias
    - UrlList.tsx - table/list displaying all shortened URLs
    - UrlItem.tsx - individual URL row with copy and delete actions
  /services
    - api.ts - axios instance and API endpoint functions
  /types
    - types.ts - TypeScript interfaces for API requests/responses
  /utils
    - validation.ts - URL validation helpers
  App.tsx - main app component with routing
  theme.ts - MUI theme configuration
```

### Key Features
- Form to shorten URLs with optional custom alias
- Display list of all shortened URLs in a table
- Copy shortened URL to clipboard
- Delete shortened URLs
- Error handling and user feedback (snackbar notifications)
- Responsive design using MUI Grid/Box

### API Integration
- Base URL configured via environment variables
- Error handling for failed requests
- Loading states during API calls

### State Management
- React hooks (useState, useEffect)
- Consider Context API if state sharing becomes complex

## Testing

### Backend Tests
- Spring application testing: controller, service, and repository layers
- Integration tests
- JUnit and Mockito with Spring framework

### Frontend Tests
- React app testing with Jest and React Testing Library
- Component unit tests
- Integration tests for API calls

## TODO

### Current Sprint
- [ ] Initialize React app with TypeScript and Material UI
- [ ] Set up project structure and routing
- [ ] Implement UrlShortenerForm component
- [ ] Implement UrlList and UrlItem components
- [ ] Create API service layer
- [ ] Add error handling and notifications
- [ ] Write frontend tests
- [ ] Dockerize frontend application
- [ ] Update docker-compose.yml with all services

### Possible Future Enhancements
- Observability (logging, monitoring, metrics)
- Testing reports and coverage
- Security (authentication, rate limiting, input sanitization)
- Environment splitting (dev vs production)
- Secret management (pull secrets from secure store in build)
- Analytics (track click counts per shortened URL)