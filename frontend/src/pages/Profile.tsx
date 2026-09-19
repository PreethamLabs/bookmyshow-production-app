import { useAuth } from '../context/AuthContext';
import { useState } from 'react';
import { User, Mail, Shield, Trash2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../api/auth';
import { ApiError } from '../api/client';
import { Button } from '../components/ui/Button';
import { useToast } from '../context/ToastContext';
import '../layouts/AuthLayout.css';

export default function Profile() {
  const { user, logout } = useAuth();
  const { addToast } = useToast();
  const navigate = useNavigate();
  const [deleting, setDeleting] = useState(false);

  const handleDeleteAccount = async () => {
    if (!window.confirm('Delete your account? Your booking history will be preserved, but your profile will be anonymized and you will be signed out.')) {
      return;
    }

    setDeleting(true);
    try {
      await authApi.deleteAccount();
      logout();
      addToast('success', 'Your account has been deleted.');
      navigate('/', { replace: true });
    } catch (err) {
      addToast(
        'error',
        err instanceof ApiError ? err.message : 'Unable to delete your account. Please try again.',
      );
    } finally {
      setDeleting(false);
    }
  };

  return (
    <div style={{ padding: 'var(--space-8) 0 var(--space-16)' }} className="container">
      <h1 style={{ fontSize: 'var(--font-size-2xl)', fontWeight: 700, marginBottom: 'var(--space-8)' }}>Profile</h1>

      <div style={{
        maxWidth: 480,
        background: 'var(--surface-1)',
        border: '1px solid var(--border-primary)',
        borderRadius: 'var(--radius-xl)',
        overflow: 'hidden',
      }}>
        <div style={{
          padding: 'var(--space-8)',
          textAlign: 'center',
          background: 'var(--surface-2)',
          borderBottom: '1px solid var(--border-secondary)',
        }}>
          <div style={{
            width: 72,
            height: 72,
            borderRadius: '50%',
            background: 'var(--color-primary)',
            color: 'var(--text-on-primary)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontSize: 'var(--font-size-2xl)',
            fontWeight: 800,
            margin: '0 auto var(--space-4)',
          }}>
            {user?.email?.charAt(0).toUpperCase()}
          </div>
          <h2 style={{ fontSize: 'var(--font-size-lg)', fontWeight: 600 }}>{user?.email}</h2>
        </div>

        <div style={{ padding: 'var(--space-6)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3)', padding: 'var(--space-3) 0', borderBottom: '1px solid var(--border-secondary)' }}>
            <Mail size={16} style={{ color: 'var(--text-tertiary)' }} />
            <div>
              <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--text-tertiary)' }}>Email</div>
              <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 500 }}>{user?.email}</div>
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3)', padding: 'var(--space-3) 0' }}>
            <Shield size={16} style={{ color: 'var(--text-tertiary)' }} />
            <div>
              <div style={{ fontSize: 'var(--font-size-xs)', color: 'var(--text-tertiary)' }}>Role</div>
              <div style={{ fontSize: 'var(--font-size-sm)', fontWeight: 500 }}>{user?.role}</div>
            </div>
          </div>

          <div style={{
            marginTop: 'var(--space-6)',
            paddingTop: 'var(--space-6)',
            borderTop: '1px solid var(--border-secondary)',
          }}>
            <h3 style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600, color: 'var(--color-danger)', marginBottom: 'var(--space-2)' }}>
              Delete account
            </h3>
            <p style={{ color: 'var(--text-tertiary)', fontSize: 'var(--font-size-xs)', lineHeight: 1.5, marginBottom: 'var(--space-4)' }}>
              Your booking history will be preserved, but your profile details will be anonymized and you will be signed out.
            </p>
            <Button variant="danger" size="sm" onClick={handleDeleteAccount} loading={deleting}>
              <Trash2 size={15} /> Delete Account
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
