import { useEffect } from "react";
import { useAuth } from "react-oidc-context";
import { useNavigate } from "react-router";

const CallbackPage: React.FC = () => {
  const { isLoading, isAuthenticated, error } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (error) {
      console.error("OIDC error:", error);
    }

    if (isLoading) {
      return;
    }

    if (isAuthenticated) {
      const redirectPath = localStorage.getItem("redirectPath");
      localStorage.removeItem("redirectPath");
      navigate(redirectPath || "/"); // fallback to home if missing
    }
  }, [isLoading, isAuthenticated, navigate, error]);

  if (error) {
    return <p>Authentication error: {error.message}</p>;
  }

  if (isLoading) {
    return <p>Processing login...</p>;
  }

  return <p>Completing login...</p>;
};

export default CallbackPage;