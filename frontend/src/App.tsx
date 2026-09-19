import { ThemeProvider } from './context/ThemeContext';
import { AuthProvider } from './context/AuthContext';
import { CityProvider } from './context/CityContext';
import { ToastProvider } from './context/ToastContext';
import { ToastContainer } from './components/ui/Toast';
import { AppRouter } from './routes/AppRouter';

export default function App() {
  return (
    <ThemeProvider>
      <ToastProvider>
        <AuthProvider>
          <CityProvider>
            <AppRouter />
            <ToastContainer />
          </CityProvider>
        </AuthProvider>
      </ToastProvider>
    </ThemeProvider>
  );
}
