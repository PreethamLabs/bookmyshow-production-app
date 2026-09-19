import { Link } from 'react-router-dom';
import { Home, Film } from 'lucide-react';
import { Button } from '../components/ui/Button';

export default function NotFound() {
  return (
    <div style={{
      minHeight: '80vh',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      textAlign: 'center',
      padding: 'var(--space-8)',
      gap: 'var(--space-4)',
    }}>
      <div style={{
        fontSize: '8rem',
        fontWeight: 900,
        lineHeight: 1,
        background: 'linear-gradient(135deg, var(--color-primary), #7c3aed)',
        WebkitBackgroundClip: 'text',
        WebkitTextFillColor: 'transparent',
        backgroundClip: 'text',
      }}>
        404
      </div>
      <h1 style={{ fontSize: 'var(--font-size-xl)', fontWeight: 700 }}>
        Page not found
      </h1>
      <p style={{ color: 'var(--text-tertiary)', maxWidth: 400 }}>
        The page you're looking for doesn't exist or has been moved.
      </p>
      <Link to="/">
        <Button variant="primary">
          <Home size={16} /> Back to Home
        </Button>
      </Link>
    </div>
  );
}
