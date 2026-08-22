/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.fileclassifier;

import cn.zhuatech.fileclassifier.service.WorkspaceService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WorkspaceServiceTests {
    private final WorkspaceService service = new WorkspaceService();

    @Test
    void returnsReviewableDomainResult() {
        var request = new WorkspaceService.RunRequest("华东仓验收清单-v3.xlsx", "项目交付文件归档", 75, true, "演示上下文");
        var result = service.run(request);
        assertThat(result.status()).isEqualTo("REVIEW_READY");
        assertThat(result.executionMode()).isEqualTo("LOCAL_DEMO_PIPELINE");
        assertThat(result.insights()).hasSize(3);
        assertThat(result.actions()).hasSize(3);
        assertThat(result.providerPayload()).containsEntry("provider", "deepseek-compatible");
    }

    @Test
    void blocksDirectAutomationWithoutHumanReview() {
        var request = new WorkspaceService.RunRequest("华东仓验收清单-v3.xlsx", "项目交付文件归档", 60, false, "");
        var result = service.run(request);
        assertThat(result.status()).isEqualTo("HUMAN_REVIEW_REQUIRED");
        assertThat(result.warnings()).hasSizeGreaterThanOrEqualTo(2);
    }
}
