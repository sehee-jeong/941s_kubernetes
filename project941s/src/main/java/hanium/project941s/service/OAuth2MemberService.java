package hanium.project941s.service;

import hanium.project941s.domain.Enums.Role;
import hanium.project941s.dto.OAuthAttributes;
import hanium.project941s.dto.PrincipalDetails;
import hanium.project941s.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hanium.project941s.domain.Member;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuth2MemberService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // name 생성
        String provider = userRequest.getClientRegistration().getRegistrationId(); //google
        String providerId = oAuth2User.getAttribute("sub");
        if (provider.equals("github")) providerId = oAuth2User.getAttribute("id").toString();
        String findByMemberProviderId = provider + "_" + providerId;
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Role role = Role.USER; //일반 유저

        List<Member> members = memberRepository.findByMemberProviderId(findByMemberProviderId);
        Member member;
        if (members.isEmpty()) { //최초 로그인
            try {
                member = OAuthAttributes.of(provider, attributes, role).toEntity();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            memberRepository.save(member);
        } else{ // 기존 고객
            member = members.get(0);
        }

        return new PrincipalDetails(member,  oAuth2User.getAttributes());
    }
}
