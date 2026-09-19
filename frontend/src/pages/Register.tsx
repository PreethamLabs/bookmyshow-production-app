import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, Lock, User, Phone, Film } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../context/ToastContext';
import { Input } from '../components/ui/Input';
import { Button } from '../components/ui/Button';
import { ApiError } from '../api/client';
import { authApi } from '../api/auth';
import '../layouts/AuthLayout.css';

export default function Register() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [verificationStep, setVerificationStep] = useState(false);
  const [otp, setOtp] = useState('');
  const [otpLoading, setOtpLoading] = useState(false);

  const { register } = useAuth();
  const { addToast } = useToast();
  const navigate = useNavigate();

  const validate = () => {
    const e: Record<string, string> = {};
    if (!name.trim()) e.name = 'Name is required';
    if (!email.trim()) e.email = 'Email is required';
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim())) {
      e.email = 'Please enter a valid email address';
    }
    if (!password || password.length < 4) e.password = 'Password must be at least 4 characters';
    
    const cleanPhone = phoneNumber.trim().replace(/[\s-]/g, '');
    if (!cleanPhone) {
      e.phoneNumber = 'Phone number is required';
    } else {
      const isTenDigit = /^[6-9]\d{9}$/.test(cleanPhone);
      const isE164 = /^\+[1-9]\d{7,14}$/.test(cleanPhone);
      if (!isTenDigit && !isE164) {
        e.phoneNumber = 'Enter a valid 10-digit mobile number or international number with +';
      }
    }
    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    let formattedPhone = phoneNumber.trim().replace(/[\s-]/g, '');
    if (/^[6-9]\d{9}$/.test(formattedPhone)) {
      formattedPhone = `+91${formattedPhone}`;
    }

    setLoading(true);
    try {
      const normalizedEmail = email.trim().toLowerCase();
      await register({
        name: name.trim(),
        email: normalizedEmail,
        password,
        phoneNumber: formattedPhone,
      });
      setVerificationStep(true);
      try {
        await authApi.sendVerification(normalizedEmail);
        addToast('success', 'We sent a verification code to your email.');
      } catch {
        addToast('error', 'Account created, but the verification email could not be sent. Please try again.');
      }
    } catch (err: unknown) {
      let msg = 'Registration failed. Please try again.';
      if (err instanceof ApiError) {
        if (err.status === 409 || err.message.toLowerCase().includes('already exists') || err.message.toLowerCase().includes('duplicate')) {
          msg = 'An account with this email or phone number already exists. Please use different details or sign in.';
        } else if (err.status === 401 || err.status === 403) {
          msg = 'Unable to create account. Please ensure all details are correct or try signing in.';
        } else {
          msg = err.message || msg;
        }
      }
      addToast('error', msg);
    } finally {
      setLoading(false);
    }
  };

  const handleVerify = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!/^\d{6}$/.test(otp)) {
      setErrors({ otp: 'Enter the 6-digit code from your email' });
      return;
    }

    setOtpLoading(true);
    try {
      await authApi.verifyEmail(email.trim().toLowerCase(), otp);
      addToast('success', 'Email verified successfully. You can now sign in.');
      navigate('/login');
    } catch (err: unknown) {
      const message = err instanceof ApiError
        ? err.message
        : 'The code is invalid or expired. Please try again.';
      setErrors({ otp: message });
    } finally {
      setOtpLoading(false);
    }
  };

  const handleResend = async () => {
    setOtpLoading(true);
    try {
      await authApi.sendVerification(email.trim().toLowerCase());
      setErrors({});
      addToast('success', 'A new verification code was sent to your email.');
    } catch (err: unknown) {
      addToast('error', err instanceof ApiError ? err.message : 'Could not resend the verification code.');
    } finally {
      setOtpLoading(false);
    }
  };

  return (
    <div className="cv-auth-layout">
      <div className="cv-auth-card">
        <Link to="/" className="cv-auth-logo">
          <span className="cv-navbar-logo-icon">
            <Film size={18} />
          </span>
          Marquee
        </Link>
        <p className="cv-auth-subtitle">
          {verificationStep
            ? `Enter the 6-digit code sent to ${email.trim().toLowerCase()}`
            : 'Create your account and start booking'}
        </p>

        {verificationStep ? (
          <form className="cv-auth-form" onSubmit={handleVerify}>
            <Input
              label="Email verification code"
              type="text"
              inputMode="numeric"
              maxLength={6}
              placeholder="123456"
              value={otp}
              onChange={e => setOtp(e.target.value.replace(/\D/g, ''))}
              error={errors.otp}
              autoComplete="one-time-code"
            />
            <Button type="submit" fullWidth loading={otpLoading}>
              Verify Email
            </Button>
            <Button type="button" fullWidth variant="secondary" onClick={handleResend} loading={otpLoading}>
              Resend Code
            </Button>
          </form>
        ) : (
        <form className="cv-auth-form" onSubmit={handleSubmit}>
          <Input
            label="Full Name"
            type="text"
            placeholder="John Doe"
            icon={<User size={16} />}
            value={name}
            onChange={e => setName(e.target.value)}
            error={errors.name}
            autoComplete="name"
          />
          <Input
            label="Email"
            type="email"
            placeholder="you@example.com"
            icon={<Mail size={16} />}
            value={email}
            onChange={e => setEmail(e.target.value)}
            error={errors.email}
            autoComplete="email"
          />
          <Input
            label="Phone Number"
            type="tel"
            placeholder="9876543210 or +919876543210"
            icon={<Phone size={16} />}
            value={phoneNumber}
            onChange={e => setPhoneNumber(e.target.value)}
            error={errors.phoneNumber}
            autoComplete="tel"
          />
          <Input
            label="Password"
            type="password"
            placeholder="••••••••"
            icon={<Lock size={16} />}
            value={password}
            onChange={e => setPassword(e.target.value)}
            error={errors.password}
            autoComplete="new-password"
          />
          <Button type="submit" fullWidth loading={loading}>
            Create Account
          </Button>
        </form>
        )}

        <p className="cv-auth-footer">
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </div>
    </div>
  );
}
