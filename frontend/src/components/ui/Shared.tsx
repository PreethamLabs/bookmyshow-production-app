import { type ReactNode, type CSSProperties } from 'react';
import { AlertTriangle, PackageOpen } from 'lucide-react';
import { Button } from './Button';
import './Shared.css';

// ---- Skeleton ----
interface SkeletonProps {
  variant?: 'text' | 'title' | 'avatar' | 'card' | 'rect';
  width?: string | number;
  height?: string | number;
  count?: number;
}

export function Skeleton({ variant = 'text', width, height, count = 1 }: SkeletonProps) {
  const style: CSSProperties = {};
  if (width) style.width = typeof width === 'number' ? `${width}px` : width;
  if (height) style.height = typeof height === 'number' ? `${height}px` : height;

  return (
    <>
      {Array.from({ length: count }).map((_, i) => (
        <div key={i} className={`cv-skeleton cv-skeleton--${variant}`} style={style} />
      ))}
    </>
  );
}

// ---- Badge ----
interface BadgeProps {
  variant?: 'success' | 'danger' | 'warning' | 'info' | 'neutral';
  children: ReactNode;
}

export function Badge({ variant = 'neutral', children }: BadgeProps) {
  return <span className={`cv-badge cv-badge--${variant}`}>{children}</span>;
}

// ---- Spinner ----
export function Spinner() {
  return (
    <div className="cv-spinner-container">
      <div className="cv-spinner" />
    </div>
  );
}

// ---- Empty State ----
interface EmptyStateProps {
  icon?: ReactNode;
  title: string;
  description?: string;
  action?: ReactNode;
}

export function EmptyState({ icon, title, description, action }: EmptyStateProps) {
  return (
    <div className="cv-empty-state animate-fade-in-up">
      <div className="cv-empty-state-icon">{icon || <PackageOpen size={28} />}</div>
      <h3 className="cv-empty-state-title">{title}</h3>
      {description && <p className="cv-empty-state-desc">{description}</p>}
      {action}
    </div>
  );
}

// ---- Error State ----
interface ErrorStateProps {
  title?: string;
  message?: string;
  onRetry?: () => void;
}

export function ErrorState({
  title = 'Something went wrong',
  message = 'We encountered an unexpected error. Please try again.',
  onRetry,
}: ErrorStateProps) {
  return (
    <div className="cv-error-state animate-fade-in-up">
      <div className="cv-error-state-icon">
        <AlertTriangle size={28} />
      </div>
      <h3 className="cv-error-state-title">{title}</h3>
      <p className="cv-error-state-desc">{message}</p>
      {onRetry && (
        <Button variant="secondary" onClick={onRetry}>
          Try Again
        </Button>
      )}
    </div>
  );
}
