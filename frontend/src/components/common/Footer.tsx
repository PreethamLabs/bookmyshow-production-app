import { Link } from 'react-router-dom';
import { Film } from 'lucide-react';
import './Footer.css';

export function Footer() {
  return (
    <footer className="cv-footer">
      <div className="cv-footer-inner container">
        <div className="cv-footer-brand">
          <Link to="/" className="cv-navbar-logo" style={{ fontSize: '1.125rem' }}>
            <span className="cv-navbar-logo-icon" style={{ width: 28, height: 28 }}>
              <Film size={14} />
            </span>
            Marquee
          </Link>
          <p>
            Your premium destination for movie tickets. Discover, book, and enjoy the cinema experience across India.
          </p>
        </div>

        <div>
          <h4 className="cv-footer-title">Explore</h4>
          <div className="cv-footer-links">
            <Link to="/">Movies</Link>
            <Link to="/my-bookings">My Bookings</Link>
            <Link to="/profile">Profile</Link>
          </div>
        </div>

        <div>
          <h4 className="cv-footer-title">Help</h4>
          <div className="cv-footer-links">
            <a href="#">FAQs</a>
            <a href="#">Contact Us</a>
            <a href="#">Terms of Use</a>
          </div>
        </div>

        <div>
          <h4 className="cv-footer-title">Company</h4>
          <div className="cv-footer-links">
            <a href="#">About</a>
            <a href="#">Privacy Policy</a>
            <a href="#">Careers</a>
          </div>
        </div>
      </div>

      <div className="container">
        <div className="cv-footer-bottom">
          <span>© {new Date().getFullYear()} Marquee. All rights reserved.</span>
          <span>Made with care in India</span>
        </div>
      </div>
    </footer>
  );
}
